package com.garden.data.datasource.local

import androidx.paging.PagingSource
import com.garden.data.entity.PlantEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository module for handling data operations.
 */
interface PlantLocalDataSource {

    fun getPlants(query: String): PagingSource<Int, PlantEntity>

    fun getPlant(plantId: Int): Flow<PlantEntity>

    suspend fun upsertAll(plants: List<PlantEntity>)

    suspend fun clearAll()
}
