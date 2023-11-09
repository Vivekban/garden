package com.garden.domain.model

import com.garden.data.entity.PlantEntity

/**
 * This class captures the relationship between a [PlantEntity] and a user's [Planting], which is
 * used by Room to fetch the related entities.
 */
data class PlantAndPlantings(
    val plant: Plant,

    val plantings: List<Planting> = emptyList()
)
