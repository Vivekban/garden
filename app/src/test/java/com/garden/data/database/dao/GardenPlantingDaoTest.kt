package com.garden.data.database.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.garden.data.database.AppDatabase
import com.garden.data.entity.PlantingEntity
import com.garden.data.mappers.toEntity
import com.garden.domain.model.Planting
import com.garden.fake.model.testCalendar
import com.garden.fake.model.testPlant
import com.garden.fake.model.testPlanting
import com.garden.fake.model.testPlants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class GardenPlantingDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var plantingDao: PlantingDao
    private var testGardenPlantingId: Long = 0

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        plantingDao = database.gardenPlantingDao()

        database.plantDao().upsertAll(testPlants.map { it.toEntity() })
        testGardenPlantingId = plantingDao.insertGardenPlanting(testPlanting.toEntity())
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetGardenPlantings() = runBlocking {
        val planting2 = PlantingEntity(
            2,
            testPlants[1].id,
            testCalendar,
            testCalendar
        )
        plantingDao.insertGardenPlanting(planting2)
        assertThat(plantingDao.getGardenPlantings().first().size, equalTo(2))
    }

    @Test
    fun testDeleteGardenPlanting() = runBlocking {
        val planting2 = PlantingEntity(
            2,
            testPlants[1].id,
            testCalendar,
            testCalendar
        )
        plantingDao.insertGardenPlanting(planting2)
        assertThat(plantingDao.getGardenPlantings().first().size, equalTo(2))
        plantingDao.deleteGardenPlanting(planting2)
        assertThat(plantingDao.getGardenPlantings().first().size, equalTo(1))
    }

    @Test
    fun testGetGardenPlantingForPlant() = runBlocking {
        assertTrue(plantingDao.isPlanted(testPlant.id).first())
    }

    @Test
    fun testGetGardenPlantingForPlant_notFound() = runBlocking {
        assertFalse(plantingDao.isPlanted(testPlants[2].id).first())
    }

    @Test
    fun testGetPlantAndGardenPlantings() = runBlocking {
        val plantAndGardenPlantings = plantingDao.getPlantedGardens().first()
        assertThat(plantAndGardenPlantings.size, equalTo(1))

        /**
         * Only the [testPlant] has been planted, and thus has an associated [Planting]
         */
        assertThat(plantAndGardenPlantings[0].plant, equalTo(testPlant.toEntity()))
        assertThat(plantAndGardenPlantings[0].plantings.size, equalTo(1))
    }
}
