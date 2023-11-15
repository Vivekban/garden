package com.garden.utilities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import com.garden.data.entity.PlantAndPlantingsEntity
import com.garden.data.entity.PlantEntity
import com.garden.domain.model.Plant
import com.garden.domain.model.PlantAndPlantings
import com.garden.domain.model.Planting
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
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

/**
 * Returns the content description for the navigation button view in the toolbar.
 */
fun getToolbarNavigationContentDescription(activity: Activity, toolbarId: Int) =
    activity.findViewById<Toolbar>(toolbarId).navigationContentDescription as String

/**
 * Simplify testing Intents with Chooser
 *
 * @param matcher the actual intent before wrapped by Chooser Intent
 */
fun chooser(matcher: Matcher<Intent>): Matcher<Intent> = allOf(
    hasAction(Intent.ACTION_CHOOSER),
    hasExtra(`is`(Intent.EXTRA_INTENT), matcher)
)
