package com.garden.compose.plantlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.garden.compose.plantdetail.plantForTesting
import com.garden.domain.model.Plant
import com.garden.presentation.plantlist.PlantListScreen
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun plantList_itemShown() {
        startPlantList()
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
    }

    private fun startPlantList(onPlantClick: (Plant) -> Unit = {}) {
        composeTestRule.setContent {
            PlantListScreen(
                plants = flowOf(PagingData.from(listOf(plantForTesting())))
                    .collectAsLazyPagingItems(),
                searchQuery = null,
                onPlantClick = onPlantClick
            )
        }
    }
}
