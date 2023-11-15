package com.garden.data.datasource.remote

import com.garden.common.Constant
import com.garden.data.dto.PlantDto
import com.garden.data.dto.PlantListDto

/**
 * Responsible for providing plant related information from cloud.
 */
interface PlantRemoteDataSource {

    /**
     * Fetches list of plants ordered by id, page size is [Constant.ITEMS_PER_PAGE]
     *
     * @param query search plants by name
     * @param page for pagination, start from [Constant.PAGE_STARTING_INDEX]
     */
    suspend fun getPlants(query: String?, page: Int): PlantListDto

    /**
     * Fetch single plant based on its [id]
     */
    suspend fun getPlant(id: Int): PlantDto
}
