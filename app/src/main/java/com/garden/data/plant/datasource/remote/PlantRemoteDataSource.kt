package com.garden.data.plant.datasource.remote

import com.garden.data.common.DataConstant
import com.garden.data.plant.dto.PlantDto
import com.garden.data.plant.dto.PlantListDto

/**
 * Responsible for providing plant related information from cloud.
 */
interface PlantRemoteDataSource {

    /**
     * Fetches list of plants ordered by id, page size is [DataConstant.ITEMS_PER_PAGE]
     *
     * @param query search plants by name
     * @param page for pagination, start from [DataConstant.PAGE_STARTING_INDEX]
     */
    suspend fun getPlants(query: String?, page: Int): PlantListDto

    /**
     * Fetch single plant based on its [id]
     */
    suspend fun getPlant(id: Int): PlantDto
}
