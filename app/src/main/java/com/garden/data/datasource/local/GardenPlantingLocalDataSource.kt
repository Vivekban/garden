package com.garden.data.datasource.local

import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.model.Planting
import kotlinx.coroutines.flow.Flow

interface GardenPlantingLocalDataSource {

    suspend fun createGardenPlanting(plantId: String)

    suspend fun removeGardenPlanting(planting: Planting)

    fun isPlanted(plantId: String): Flow<Boolean>

    fun getPlantedGardens(): Flow<List<PlantAndPlantings>>

}

