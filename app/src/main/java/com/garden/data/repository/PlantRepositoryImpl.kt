package com.garden.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.RoomDatabase
import com.garden.common.Constant
import com.garden.data.datasource.local.PlantLocalDataSource
import com.garden.data.datasource.local.RemoteKeysLocalDataSource
import com.garden.data.datasource.remote.PlantRemoteDataSource
import com.garden.data.datasource.remote.mediator.PlantRemoteMediator
import com.garden.data.mappers.toModel
import com.garden.domain.model.Plant
import com.garden.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class PlantRepositoryImpl @Inject constructor(
    private val database: RoomDatabase,
    private val remote: PlantRemoteDataSource,
    private val local: PlantLocalDataSource,
    private val remoteKeysLocalDataSource: RemoteKeysLocalDataSource,
) : PlantRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPlants(query: String): Flow<PagingData<Plant>> {

        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { local.getPlants(dbQuery) }

        return Pager(
            config = PagingConfig(
                Constant.ITEM_PER_PAGE,
                initialLoadSize = Constant.ITEM_PER_PAGE,
                prefetchDistance = 1
            ),
            remoteMediator = PlantRemoteMediator(
                query,
                database,
                remote,
                local,
                remoteKeysLocalDataSource
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { it.map { plant -> plant.toModel() } }
    }

    override fun getPlant(plantId: Int) = local.getPlant(plantId).map { it.toModel() }
}
