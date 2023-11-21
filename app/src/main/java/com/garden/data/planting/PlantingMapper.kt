package com.garden.data.planting

import com.garden.domain.planting.Planting

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
