package com.garden.data.database.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.garden.common.Constant
import com.garden.data.database.AppDatabase
import com.garden.data.entity.PlantEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlantDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var plantDao: PlantDao

    private val plantA = PlantEntity(1, "p A", "", 1, 1, "")
    private val plantB = PlantEntity(2, "p B", "", 1, 1, "")
    private val plantC = PlantEntity(3, "p C", "", 2, 2, "")

    private val query = ""

    @Before
    fun createDb() = runBlocking {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
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
        val plantList: PagingSource<Int, PlantEntity> =
            plantDao.getPlants("%${query.replace(' ', '%')}%")

        val expectedResult = PagingSource.LoadResult.Page(
            data = listOf(plantA, plantB, plantC),
            prevKey = null,
            nextKey = null
        )

        val result = plantList.load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = Constant.ITEMS_PER_PAGE,
                placeholdersEnabled = false
            )
        )

        assertThat((result as PagingSource.LoadResult.Page).data, equalTo(expectedResult.data))
    }

    @Test
    fun testGetPlant() = runBlocking {
        assertThat(plantDao.getPlant(plantA.plantId).first(), equalTo(plantA))
    }
}
