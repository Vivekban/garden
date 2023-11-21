package com.garden.data.plant.datasource.local.impl

import com.garden.data.plant.dao.PlantDao
import com.garden.data.plant.datasource.local.PlantLocalDataSourceImpl
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlantLocalDataSourceImplTest {

    private lateinit var plantLocalDataSourceImpl: PlantLocalDataSourceImpl

    @Mock
    private lateinit var plantDao: PlantDao

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getPlants() {
    }

    @Test
    fun getPlant() {
    }

    @Test
    fun upsertAll() {
    }

    @Test
    fun clearAll() {
    }
}
