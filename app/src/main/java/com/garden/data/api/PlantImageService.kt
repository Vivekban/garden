package com.garden.data.api

import com.garden.BuildConfig
import com.garden.data.dto.UnsplashSearchDto
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface PlantImageService {

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchDto

    companion object {

        fun create(retrofit: Retrofit): PlantImageService {
            return retrofit.create(PlantImageService::class.java)
        }
    }
}