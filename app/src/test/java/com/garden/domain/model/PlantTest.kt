package com.garden.domain.model

import com.garden.domain.model.Plant
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlantTest {

    private lateinit var plant: Plant

    @Before
    fun setUp() {
        plant =
            Plant(1, "Tomato", "A red vegetable", 1, wateringInterval = 7, imageUrl = "")
    }

    @Test
    fun test_default_values() {
        assertEquals(7, plant.wateringInterval)
        assertEquals("", plant.imageUrl)
    }

    @Test
    fun test_toString() {
        assertEquals("Tomato", plant.toString())
    }
}
