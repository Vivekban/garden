package com.garden.data.plantandplanting

import com.garden.data.plant.toEntity
import com.garden.data.plant.toModel
import com.garden.data.planting.toEntity
import com.garden.data.planting.toModel
import com.garden.domain.plantandplanting.PlantAndPlantings

fun PlantAndPlantingsEntity.toModel() = PlantAndPlantings(
    plant.toModel(),
    plantings.map { it.toModel() }
)

fun PlantAndPlantings.toEntity() = PlantAndPlantingsEntity(
    plant.toEntity(),
    plantings.map { it.toEntity() }
)
