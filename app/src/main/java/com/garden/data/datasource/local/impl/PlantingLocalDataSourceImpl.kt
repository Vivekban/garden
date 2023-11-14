package com.garden.data.datasource.local.impl

import androidx.paging.PagingSource
import com.garden.data.database.DatabaseUtility
import com.garden.data.database.dao.PlantingDao
import com.garden.data.datasource.local.PlantingLocalDataSource
import com.garden.data.entity.PlantAndPlantingsEntity
import com.garden.data.entity.PlantingEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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
