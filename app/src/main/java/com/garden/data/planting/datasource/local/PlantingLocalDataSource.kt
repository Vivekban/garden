package com.garden.data.planting.datasource.local

import androidx.paging.PagingSource
import com.garden.data.plantandplanting.PlantAndPlantingsEntity
import com.garden.data.planting.PlantingEntity
import kotlinx.coroutines.flow.Flow

interface PlantingLocalDataSource {

    suspend fun createGardenPlanting(planting: PlantingEntity): Long

    suspend fun removeGardenPlanting(planting: PlantingEntity)

    fun isPlanted(plantId: Int): Flow<Boolean>

    fun getPlantedGardens(query: String?): PagingSource<Int, PlantAndPlantingsEntity>
}
