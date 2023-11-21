package com.garden.presentation.plantdetail

import android.content.ContentResolver
import android.net.Uri
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.garden.domain.plant.Plant
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantDetailComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun plantDetails_checkIsNotPlanted() {
        startPlantDetails(isPlanted = false)
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add plant").assertIsDisplayed()
    }

    @Test
    fun plantDetails_checkIsPlanted() {
        startPlantDetails(isPlanted = true)
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Add plant").assertDoesNotExist()
    }

    private fun startPlantDetails(isPlanted: Boolean) {
        composeTestRule.setContent {
            PlantDetails(
                plant = plantForTesting(),
                isPlanted = isPlanted,
                callbacks = PlantDetailsCallbacks({ }, { }, { }, {})
            )
        }
    }
}

@Composable
internal fun plantForTesting(): Plant {
    return Plant(
        id = 1,
        name = "Apple",
        description = "An apple is a sweet, edible fruit produced by an apple tree ." +
            "Apple trees are cultivated worldwide, and are the most widely grown species" +
            " in the genus Malus. The tree originated",
        growZoneNumber = 3,
        wateringInterval = 30
    )
}

/**
 * Returns the Uri of a given raw resource
 */
@Composable
private fun rawUri(@RawRes id: Int): Uri {
    return "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${LocalContext.current.packageName}/$id"
        .toUri()
}
