package com.garden.data.mappers

import com.garden.data.entity.PlantAndPlantingsEntity
import com.garden.domain.model.PlantAndPlantings

fun PlantAndPlantingsEntity.toModel() = PlantAndPlantings(
    plant.toModel(),
    plantings.map { it.toModel() }
)

fun PlantAndPlantings.toEntity() = PlantAndPlantingsEntity(
    plant.toEntity(),
    plantings.map { it.toEntity() }
)
