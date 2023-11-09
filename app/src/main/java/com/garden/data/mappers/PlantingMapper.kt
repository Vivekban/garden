package com.garden.data.mappers

import com.garden.data.entity.PlantingEntity
import com.garden.domain.model.Planting

fun PlantingEntity.toModel() = Planting(
    plantId = plantId,
    plantDate = plantDate,
    lastWateringDate = lastWateringDate,
    id = id
)

fun Planting.toEntity() = PlantingEntity(
    id = id,
    plantId = plantId,
    plantDate,
    lastWateringDate
)