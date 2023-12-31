package com.garden.presentation.plantdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.garden.MainCoroutineRule
import com.garden.data.database.AppDatabase
import com.garden.domain.plant.usecase.GetPlantUsecase
import com.garden.domain.planting.usecase.AddPlantToGardenUsecase
import com.garden.domain.planting.usecase.IsPlantPlantedUsecase
import com.garden.runBlockingTest
import com.garden.utilities.testPlant
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class PlantDetailViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: PlantDetailViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule: RuleChain = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)
        .around(coroutineRule)

    @Inject
    lateinit var getPlantUsecase: GetPlantUsecase

    @Inject
    lateinit var isPlantPlantedUsecase: IsPlantPlantedUsecase

    @Inject
    lateinit var addPlantToGardenUsecase: AddPlantToGardenUsecase

    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        val savedStateHandle: SavedStateHandle = SavedStateHandle().apply {
            set("plantId", testPlant.id)
        }
        viewModel =
            PlantDetailViewModel(
                savedStateHandle,
                getPlantUsecase,
                isPlantPlantedUsecase,
                addPlantToGardenUsecase
            )
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() = coroutineRule.runBlockingTest {
        val result = viewModel.uiState.first().isPlanted
        assertFalse(result)
    }
}
