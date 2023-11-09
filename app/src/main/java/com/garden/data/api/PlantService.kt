package com.garden.data.api

import com.garden.data.dto.PlantListDto
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Used to connect to the API to fetch plant list
 */
interface PlantService {

    @GET("species-list")
    suspend fun getPlants(
        @Query("q") query: String?,
        @Query("page") page: Int?,
    ): PlantListDto

    companion object {

        fun create(retrofit: Retrofit): PlantService {
            return retrofit.create(PlantService::class.java)
        }
    }
}