package com.garden.data.plant.datasource.local

import androidx.paging.PagingSource
import com.garden.data.database.DatabaseUtility
import com.garden.data.plant.PlantEntity
import com.garden.data.plant.dao.PlantDao
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Local source of Plant related information
 *
 *  * Collecting from the Flows in [PlantDao] is main-safe.  Room supports Coroutines and moves the
 *  * query execution off of the main thread.
 */
class PlantLocalDataSourceImpl @Inject constructor(private val dao: PlantDao) :
    PlantLocalDataSource {

    override fun getPlants(query: String): PagingSource<Int, PlantEntity> =
        dao.getPlants(DatabaseUtility.toDatabaseQuery(query))

    override fun getPlant(plantId: Int): Flow<PlantEntity> = dao.getPlant(plantId)
    override suspend fun upsertAll(plants: List<PlantEntity>) = dao.upsertAll(plants)

    override suspend fun clearAll() = dao.clearAll()
}
