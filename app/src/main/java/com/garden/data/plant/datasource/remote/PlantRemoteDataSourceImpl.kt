package com.garden.data.plant.datasource.remote

import com.garden.data.plant.PlantService
import com.garden.data.plant.dto.PlantDto
import com.garden.data.plant.dto.PlantListDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [PlantRemoteDataSource] uses [plantService] to fetch plant information
 * from cloud
 */
@Singleton
class PlantRemoteDataSourceImpl @Inject constructor(
    private val plantService: PlantService
) : PlantRemoteDataSource {

    override suspend fun getPlants(query: String?, page: Int): PlantListDto =
        plantService.getPlants(query, page)

    override suspend fun getPlant(id: Int): PlantDto = plantService.getPlant(id)
}
