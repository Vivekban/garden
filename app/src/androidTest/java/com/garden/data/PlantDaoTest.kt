package com.garden.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.garden.common.Constant
import com.garden.data.database.AppDatabase
import com.garden.data.database.dao.PlantDao
import com.garden.data.entity.PlantEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var plantDao: PlantDao

    private val plantA = PlantEntity(1, "A", "", 1, 1, "")
    private val plantB = PlantEntity(2, "B", "", 1, 1, "")
    private val plantC = PlantEntity(3, "C", "", 2, 2, "")

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        plantDao = database.plantDao()

        // Insert plants in non-alphabetical order to test that results are sorted by name
        plantDao.upsertAll(listOf(plantB, plantC, plantA))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetPlants() = runBlocking {

        assertThat(plantDao.getPlant(plantA.plantId.toString()).first(), equalTo(plantA))

        val plantList = plantDao.getPlants("")

        val expectedResult = PagingSource.LoadResult.Page(
            data = listOf(plantA, plantB, plantC),
            prevKey = null,
            nextKey = 1
        )

        val result = plantList.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = Constant.ITEM_PER_PAGE,
                placeholdersEnabled = false
            )
        )

        assertThat(result, equalTo(expectedResult))
    }

    @Test
    fun testGetPlant() = runBlocking {
        assertThat(plantDao.getPlant(plantA.plantId.toString()).first(), equalTo(plantA))
    }
}
