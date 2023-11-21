package com.garden.data.plant

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [PlantEntity] represents information of a Plant like [name], [description].
 */
@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val plantId: Int,
    val name: String,
    val description: String,
    val growZoneNumber: Int,
    val wateringInterval: Int,
    val imageUrl: String?
)
