package com.garden.domain.plant

import androidx.paging.PagingData
import com.garden.data.plant.dao.PlantDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository module for handling data operations.
 *
 * Collecting from the Flows in [PlantDao] is main-safe.  Room supports Coroutines and moves the
 * query execution off of the main thread.
 */
interface PlantRepository {

    fun getPlants(query: String): Flow<PagingData<Plant>>

    fun getPlant(plantId: Int): Flow<Plant>
}
