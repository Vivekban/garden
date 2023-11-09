package com.garden.domain.model

import com.garden.data.entity.PlantEntity
import java.util.Calendar

/**
 * [Planting] represents when a user adds a [PlantEntity] to their garden, with useful metadata.
 */

data class Planting(
    var id: Long = 0,

    val plantId: String,

    /**
     * Indicates when the [PlantEntity] was planted. Used for showing notification when it's time
     * to harvest the plant.
     */
    val plantDate: Calendar = Calendar.getInstance(),

    /**
     * Indicates when the [PlantEntity] was last watered. Used for showing notification when it's
     * time to water the plant.
     */
    val lastWateringDate: Calendar = Calendar.getInstance(),

    )
