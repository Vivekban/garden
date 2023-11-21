package com.garden.data.plant

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PlantServiceTest {

    private lateinit var service: PlantService
    private lateinit var server: MockWebServer

    private val fileName = "plants_response.json"
    private val searchQuery = ""
    private val page = 1

    @Before
    fun setUp() {
        server = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(PlantService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getPlants_validInput_validResponse() {
        runBlocking {
            enqueueMockResponse(fileName)
            val responseBody = service.getPlants(searchQuery, page)
            assertNotNull(responseBody)
        }
    }

    @Test
    fun getPlants_validInput_rightNumberOfItems() {
        runBlocking {
            enqueueMockResponse(fileName)
            val responseBody = service.getPlants(searchQuery, page)
            val articlesList = responseBody.data
            assertEquals(articlesList?.size, 30)
        }
    }

    @Test
    fun getPlants_validInput_throwException() {
        runBlocking {
            MockResponse().run {
                setResponseCode(400)
                server.enqueue(this)
            }

            assertThrows(HttpException::class.java) {
                runBlocking {
                    service.getPlants(searchQuery, page)
                }
            }
        }
    }

    private fun enqueueMockResponse(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }
}
