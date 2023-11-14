package com.garden.compose.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.garden.domain.model.PlantAndPlantings
import com.garden.presentation.garden.GardenScreen
import com.garden.utilities.testPlantAndGardenPlanting
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GardenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun garden_emptyGarden() {
        startGarden(emptyList())
        composeTestRule.onNodeWithTag("loader").assertIsDisplayed()
    }

    @Test
    fun garden_notEmptyGarden() {
        startGarden(listOf(testPlantAndGardenPlanting))
        composeTestRule.onNodeWithText(testPlantAndGardenPlanting.plant.name).assertIsDisplayed()
    }

    private fun startGarden(gardenPlantings: List<PlantAndPlantings>) {
        composeTestRule.setContent {
            val plants =
                flowOf(
                    PagingData.from(gardenPlantings)
                ).collectAsLazyPagingItems()
            GardenScreen(gardenPlants = plants)
        }
    }
}