package com.garden.data.datasource.local.impl

import androidx.paging.PagingSource
import com.garden.data.database.dao.PlantDao
import com.garden.data.datasource.local.PlantLocalDataSource
import com.garden.data.entity.PlantEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Local source of [Plant]
 *
 *  * Collecting from the Flows in [PlantDao] is main-safe.  Room supports Coroutines and moves the
 *  * query execution off of the main thread.
 */
class PlantLocalDataSourceImpl @Inject constructor(private val dao: PlantDao) :
    PlantLocalDataSource {

    override fun getPlants(query: String): PagingSource<Int, PlantEntity> = dao.getPlants(query)

    override fun getPlant(plantId: String): Flow<PlantEntity> = dao.getPlant(plantId)
    override suspend fun upsertAll(plants: List<PlantEntity>) = dao.upsertAll(plants)

    override suspend fun clearAll() = dao.clearAll()
}
