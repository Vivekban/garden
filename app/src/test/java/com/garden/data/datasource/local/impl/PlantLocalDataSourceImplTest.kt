package com.garden.data.datasource.local.impl

import com.garden.data.database.dao.PlantDao
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
