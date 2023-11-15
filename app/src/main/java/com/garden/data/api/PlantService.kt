package com.garden.data.api

import com.garden.data.dto.PlantDto
import com.garden.data.dto.PlantListDto
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Used to connect to the API to fetch plant list
 */
interface PlantService {

    @GET("species-list")
    suspend fun getPlants(
        @Query("q") query: String?,
        @Query("page") page: Int?
    ): PlantListDto

    @GET("species/details/{id}")
    suspend fun getPlant(@Path("id") id: Int): PlantDto

    companion object {

        fun create(retrofit: Retrofit): PlantService {
            return retrofit.create(PlantService::class.java)
        }
    }
}
