package com.garden.data.datasource.remote

import com.garden.data.database.dao.PlantDao
import com.garden.data.dto.PlantListDto

/**
 * Repository module for handling data operations.
 *
 * Collecting from the Flows in [PlantDao] is main-safe. Room supports Coroutines and moves the
 * query execution off of the main thread.
 */
interface PlantRemoteDataSource {
    suspend fun getPlants(query: String?, page: Int): PlantListDto

}
