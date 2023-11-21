package com.garden.data.plant

import com.garden.data.plant.dto.PlantDto
import com.garden.domain.plant.Plant

fun PlantDto.toEntity() = PlantEntity(
    plantId = id,
    name = name,
    description = generateDescription(),
    growZoneNumber = growZoneNumber(),
    wateringInterval = wateringInDays(),
    imageUrl = plantImageDto?.originalUrl
)

fun PlantEntity.toModel() = Plant(
    id = plantId,
    name = name,
    description = description,
    growZoneNumber = growZoneNumber,
    wateringInterval = wateringInterval,
    imageUrl = imageUrl
)

fun Plant.toEntity() = PlantEntity(
    plantId = id,
    name = name,
    description = description,
    growZoneNumber = growZoneNumber,
    wateringInterval = wateringInterval,
    imageUrl = imageUrl
)
