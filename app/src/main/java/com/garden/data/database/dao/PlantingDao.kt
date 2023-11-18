package com.garden.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.garden.data.entity.PlantAndPlantingsEntity
import com.garden.data.entity.PlantEntity
import com.garden.data.entity.PlantingEntity
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the [PlantingEntity] class.
 */
@Dao
interface PlantingDao {
    @Query("SELECT * FROM garden_plantings")
    fun getGardenPlantings(): Flow<List<PlantingEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM garden_plantings WHERE plant_id = :plantId LIMIT 1)")
    fun isPlanted(plantId: Int): Flow<Boolean>

    /**
     * This query will tell Room to query both the [PlantEntity] and [PlantingEntity] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query(
        "SELECT * FROM plants" +
            " WHERE id IN (SELECT DISTINCT(plant_id) FROM garden_plantings) " +
            "AND name like :query"
    )
    fun getPlantedGardens(query: String): PagingSource<Int, PlantAndPlantingsEntity>

    @Insert
    suspend fun insertGardenPlanting(planting: PlantingEntity): Long

    @Delete
    suspend fun deleteGardenPlanting(planting: PlantingEntity)
}
