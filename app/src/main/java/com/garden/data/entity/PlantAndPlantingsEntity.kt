package com.garden.data.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * This class captures the relationship between a [PlantEntity] and a user's [PlantingEntity], which is
 * used by Room to fetch the related entities.
 */
data class PlantAndPlantingsEntity(
    @Embedded
    val plant: PlantEntity,

    @Relation(parentColumn = "id", entityColumn = "plant_id")
    val plantings: List<PlantingEntity> = emptyList()
)
