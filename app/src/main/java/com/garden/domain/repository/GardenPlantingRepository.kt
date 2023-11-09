package com.garden.domain.repository

import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.model.Planting
import kotlinx.coroutines.flow.Flow

interface GardenPlantingRepository {

    suspend fun createGardenPlanting(plantId: String)

    suspend fun removeGardenPlanting(planting: Planting)

    fun isPlanted(plantId: String): Flow<Boolean>

    fun getPlantedGardens(): Flow<List<PlantAndPlantings>>

}

