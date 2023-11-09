package com.garden.data.api

import okhttp3.mockwebserver.MockWebServer
import org.junit.Before

class PlantServiceTest {

    private lateinit var service: PlantService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()

        val retrofit = ApiClient.getClient()
        service = PlantService.create(retrofit)
    }

//    @Test
//    fun getPlantssentRequest_receivedExpected() {
//        runBlocking {
//            enqueueMockResponse("newsresponse.json")
//            val responseBody = service.getPlants("", 1)
//            val request = server.takeRequest()
//            Assert.assertThat(responseBody).isNotNull()
//            Assert.assertThat(request.path)
//                .isEqualTo("/v2/top-headlines?country=us&page=1&apiKey=b7122b5c5f8948eda9715867b6240ce6")
//        }
//    }
//
//    @Test
//    fun getTopHeadlines_receivedResponse_correctPageSize() {
//        runBlocking {
//            enqueueMockResponse("newsresponse.json")
//            val responseBody = service.getTopHeadlines("us", 1).body()
//            val articlesList = responseBody!!.articles
//            Assert.assertThat(articlesList.size).isEqualTo(20)
//        }
//    }
//
//    private fun enqueueMockResponse(
//        fileName: String
//    ) {
//        val mockResponse = MockResponse()
//        mockResponse.setBody()
//        server.enqueue(mockResponse)
//
//    }
}