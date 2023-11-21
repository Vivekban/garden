package com.garden.domain.fake

import com.garden.data.plant.PlantEntity
import com.garden.data.plantandplanting.PlantAndPlantingsEntity
import com.garden.domain.plant.Plant
import com.garden.domain.plantandplanting.PlantAndPlantings
import com.garden.domain.planting.Planting
import java.util.Calendar

// TODO: Remove this standalone file instead of this add fake values to respective models

/**
 * Long plant description
 */
const val testPlantDescription =
    "European Silver Fir (Abies alba) is an amazing coniferous species native to mountainous" +
        " regions of central Europe and the Balkans. It is an evergreen tree with a narrow," +
        " pyramidal shape and long, soft needles. Its bark is scaly grey-brown and its branches" +
        " are highly ornamental due to its conical-shaped silver-tinged needles." +
        " It is pruned for use as an ornamental evergreen hedging and screening plant," +
        " and is also popular for use as a Christmas tree." +
        " Young trees grow quickly and have strong, flexible branches which makes them perfect" +
        " for use as windbreaks. The European Silver Fir is an impressive species, making it" +
        " ideal for gardens and public spaces."

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
