package com.garden.data.plant

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.RoomDatabase
import com.garden.data.common.DataConstant
import com.garden.data.plant.datasource.local.PlantLocalDataSource
import com.garden.data.plant.datasource.local.RemoteKeysLocalDataSource
import com.garden.data.plant.datasource.remote.PlantRemoteDataSource
import com.garden.data.plant.datasource.remote.PlantRemoteMediator
import com.garden.domain.plant.Plant
import com.garden.domain.plant.PlantRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

/**
 * Plant repository .
 */
@Singleton
class PlantRepositoryImpl @Inject constructor(
    private val database: RoomDatabase,
    private val remote: PlantRemoteDataSource,
    private val local: PlantLocalDataSource,
    private val remoteKeysLocalDataSource: RemoteKeysLocalDataSource
) : PlantRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPlants(query: String): Flow<PagingData<Plant>> {
        val pagingSourceFactory = { local.getPlants(query) }

        return Pager(
            config = PagingConfig(
                DataConstant.ITEMS_PER_PAGE,
                initialLoadSize = DataConstant.ITEMS_PER_PAGE,
                prefetchDistance = DataConstant.ITEMS_PREFETCH_DISTANCE
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

    override fun getPlant(plantId: Int): Flow<Plant> = merge(
        flow {
            try {
                val plant = remote.getPlant(id = plantId)
                val entity = plant.toEntity()
                local.upsertAll(listOf(entity))
            } catch (e: Exception) {
                Log.d("TAG", "getPlant: $e")
            }
        },
        local.getPlant(plantId)
    ).map { it.toModel() }
}
