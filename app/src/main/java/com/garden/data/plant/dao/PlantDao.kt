package com.garden.data.plant.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.garden.data.plant.PlantEntity
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface PlantDao {
    @Query("SELECT * FROM plants where name like :query ORDER BY id")
    fun getPlants(query: String?): PagingSource<Int, PlantEntity>

    @Query("SELECT * FROM plants WHERE id = :plantId")
    fun getPlant(plantId: Int): Flow<PlantEntity>

    @Upsert
    suspend fun upsertAll(plants: List<PlantEntity>)

    @Query("DELETE FROM plants")
    suspend fun clearAll()
}
