package com.garden.data.planting

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.garden.data.common.DataConstant
import com.garden.data.plantandplanting.toModel
import com.garden.data.planting.datasource.local.PlantingLocalDataSource
import com.garden.domain.plantandplanting.PlantAndPlantings
import com.garden.domain.planting.GardenPlantingRepository
import com.garden.domain.planting.Planting
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class PlantingRepositoryImpl @Inject constructor(
    private val dataSource: PlantingLocalDataSource
) : GardenPlantingRepository {

    override suspend fun createGardenPlanting(plantId: Int) {
        val planting = PlantingEntity(0, plantId)
        dataSource.createGardenPlanting(planting)
    }

    override suspend fun removeGardenPlanting(planting: Planting) {
        dataSource.removeGardenPlanting(planting.toEntity())
    }

    override fun isPlanted(plantId: Int) =
        dataSource.isPlanted(plantId)

    @ExperimentalPagingApi
    override fun getPlantedGardens(query: String?): Flow<PagingData<PlantAndPlantings>> {
        val pagingSourceFactory = { dataSource.getPlantedGardens(query) }

        return Pager(
            config = PagingConfig(
                DataConstant.ITEMS_PER_PAGE,
                initialLoadSize = DataConstant.ITEMS_PER_PAGE,
                prefetchDistance = DataConstant.ITEMS_PREFETCH_DISTANCE
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { it.map { plant -> plant.toModel() } }
    }
}
