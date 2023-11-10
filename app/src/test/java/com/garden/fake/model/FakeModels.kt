package com.garden.fake.model


import com.garden.data.entity.PlantAndPlantingsEntity
import com.garden.data.entity.PlantEntity
import com.garden.domain.model.Plant
import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.model.Planting
import java.util.Calendar

/**
 * [PlantEntity] objects used for tests.
 */
val testPlants = arrayListOf(
    Plant(1, "Apple", "A red fruit", 1),
    Plant(2, "B", "Description B", 1),
    Plant(3, "C", "Description C", 2)
)
val testPlant = testPlants[0]

/**
 * [Calendar] object used for tests.
 */
val testCalendar: Calendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, 1998)
    set(Calendar.MONTH, Calendar.SEPTEMBER)
    set(Calendar.DAY_OF_MONTH, 4)
}

/**
 * [Planting] object used for tests.
 */
val testPlanting = Planting(1, testPlant.id, testCalendar, testCalendar)

/**
 * [PlantAndPlantingsEntity] object used for tests.
 */
val testPlantAndGardenPlanting = PlantAndPlantings(testPlant, listOf(testPlanting))

