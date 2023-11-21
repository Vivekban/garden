package com.garden.domain.plantandplanting

import com.garden.data.plant.PlantEntity
import com.garden.domain.plant.Plant
import com.garden.domain.planting.Planting

/**
 * This class captures the relationship between a [PlantEntity] and a user's [Planting], which is
 * used by Room to fetch the related entities.
 */
data class PlantAndPlantings(
    val plant: Plant,

    val plantings: List<Planting> = emptyList()
)
