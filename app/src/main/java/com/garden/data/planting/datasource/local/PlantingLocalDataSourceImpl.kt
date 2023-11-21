package com.garden.data.planting.datasource.local

import androidx.paging.PagingSource
import com.garden.data.database.DatabaseUtility
import com.garden.data.plant.dao.PlantingDao
import com.garden.data.plantandplanting.PlantAndPlantingsEntity
import com.garden.data.planting.PlantingEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [PlantingLocalDataSource] uses [PlantingDao] for data.
 */
class PlantingLocalDataSourceImpl @Inject constructor(private val dao: PlantingDao) :
    PlantingLocalDataSource {

    override suspend fun createGardenPlanting(planting: PlantingEntity) =
        dao.insertGardenPlanting(planting)

    override suspend fun removeGardenPlanting(planting: PlantingEntity) =
        dao.deleteGardenPlanting(planting)

    override fun isPlanted(plantId: Int): Flow<Boolean> = dao.isPlanted(plantId)

    override fun getPlantedGardens(query: String?): PagingSource<Int, PlantAndPlantingsEntity> =
        dao.getPlantedGardens(DatabaseUtility.toDatabaseQuery(query))
}
