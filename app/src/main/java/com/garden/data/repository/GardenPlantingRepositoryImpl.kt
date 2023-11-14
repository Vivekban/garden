package com.garden.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.garden.common.Constant
import com.garden.data.datasource.local.PlantingLocalDataSource
import com.garden.data.entity.PlantingEntity
import com.garden.data.mappers.toEntity
import com.garden.data.mappers.toModel
import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.model.Planting
import com.garden.domain.repository.GardenPlantingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GardenPlantingRepositoryImpl @Inject constructor(
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
                Constant.ITEMS_PER_PAGE,
                initialLoadSize = Constant.ITEMS_PER_PAGE,
                prefetchDistance = Constant.ITEMS_PREFETCH_DISTANCE
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { it.map { plant -> plant.toModel() } }

    }

}

