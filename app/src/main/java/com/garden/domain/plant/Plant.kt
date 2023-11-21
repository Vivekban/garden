package com.garden.domain.plant

import com.garden.data.common.DataConstant
import java.util.Calendar
import java.util.Calendar.DAY_OF_YEAR
import javax.annotation.concurrent.Immutable

@Immutable
data class Plant(
    val id: Int,
    val name: String,
    val description: String,
    val growZoneNumber: Int,
    val wateringInterval: Int = DataConstant.DEFAULT_WATERING_NEED,
    val imageUrl: String? = null
) {

    /**
     * Determines if the plant should be watered.  Returns true if [since]'s date > date of last
     * watering + watering Interval; false otherwise.
     */
    fun shouldBeWatered(since: Calendar, lastWateringDate: Calendar) =
        since > lastWateringDate.apply { add(DAY_OF_YEAR, wateringInterval) }

    override fun toString() = name
}
