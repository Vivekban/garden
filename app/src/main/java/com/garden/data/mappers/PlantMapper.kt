package com.garden.data.mappers

import com.garden.data.dto.PlantDto
import com.garden.data.entity.PlantEntity
import com.garden.domain.model.Plant

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
