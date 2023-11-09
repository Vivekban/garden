package com.garden.data.datasource.remote.impl

import androidx.paging.ExperimentalPagingApi
import com.garden.data.api.PlantService
import com.garden.data.database.dao.PlantDao
import com.garden.data.datasource.remote.PlantRemoteDataSource
import com.garden.data.dto.PlantListDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 *
 * Collecting from the Flows in [PlantDao] is main-safe.  Room supports Coroutines and moves the
 * query execution off of the main thread.
 */
@Singleton
class PlantRemoteDataSourceImpl @Inject constructor(
    private val plantService: PlantService
) : PlantRemoteDataSource {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getPlants(query: String?, page: Int): PlantListDto =
        plantService.getPlants(query, page)

}
