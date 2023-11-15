package com.garden.data.datasource.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.garden.common.Constant
import com.garden.data.datasource.local.PlantLocalDataSource
import com.garden.data.datasource.local.RemoteKeysLocalDataSource
import com.garden.data.datasource.remote.PlantRemoteDataSource
import com.garden.data.entity.PlantEntity
import com.garden.data.entity.RemoteKeysEntity
import com.garden.data.mappers.toEntity
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException

/**
 * This will fetch information about plants from cloud and save it to local database.
 *
 * Local database will act as single source of truth.
 */
@OptIn(ExperimentalPagingApi::class)
class PlantRemoteMediator(
    private val query: String?,
    private val appDatabase: RoomDatabase,
    private val remote: PlantRemoteDataSource,
    private val local: PlantLocalDataSource,
    private val remoteKeysLocalDataSource: RemoteKeysLocalDataSource
) : RemoteMediator<Int, PlantEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlantEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: Constant.PAGE_STARTING_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                remoteKeys?.previousKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val result = remote.getPlants(query = query, page = page)
            val endOfPaginationReached = result.data?.isEmpty() ?: true

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysLocalDataSource.clearAll()
                }
                val prevKey = if (page == Constant.PAGE_STARTING_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = result.data?.map {
                    RemoteKeysEntity(
                        id = it.id,
                        previousKey = prevKey,
                        nextKey = nextKey
                    )
                } ?: listOf()
                remoteKeysLocalDataSource.insertAll(keys)

                val plants = result.data?.map { it.toEntity() } ?: listOf()

                local.upsertAll(plants)
            }

            return MediatorResult.Success(endOfPaginationReached = result.data?.isEmpty() ?: true)
        } catch (exception: Exception) {
            when (exception) {
                is IOException, is HttpException, is JsonSyntaxException -> {
                    return MediatorResult.Error(exception)
                }

                else -> throw exception
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PlantEntity>):
        RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                remoteKeysLocalDataSource.remoteKeysRepoId(repo.plantId)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PlantEntity>):
        RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                remoteKeysLocalDataSource.remoteKeysRepoId(repo.plantId)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PlantEntity>
    ): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.plantId?.let { repoId ->
                remoteKeysLocalDataSource.remoteKeysRepoId(repoId)
            }
        }
    }
}
