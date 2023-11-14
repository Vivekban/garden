package com.garden.domain.repository

import androidx.paging.PagingData
import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.model.Planting
import kotlinx.coroutines.flow.Flow

interface GardenPlantingRepository {

    suspend fun createGardenPlanting(plantId: Int)

    suspend fun removeGardenPlanting(planting: Planting)

    fun isPlanted(plantId: Int): Flow<Boolean>

    fun getPlantedGardens(query: String?): Flow<PagingData<PlantAndPlantings>>

}

