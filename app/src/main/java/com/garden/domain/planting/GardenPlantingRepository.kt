package com.garden.domain.planting

import androidx.paging.PagingData
import com.garden.domain.plantandplanting.PlantAndPlantings
import kotlinx.coroutines.flow.Flow

interface GardenPlantingRepository {

    suspend fun createGardenPlanting(plantId: Int)

    suspend fun removeGardenPlanting(planting: Planting)

    fun isPlanted(plantId: Int): Flow<Boolean>

    fun getPlantedGardens(query: String?): Flow<PagingData<PlantAndPlantings>>
}
