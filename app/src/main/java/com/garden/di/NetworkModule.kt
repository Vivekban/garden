package com.garden.di

import com.garden.data.api.ApiClient
import com.garden.data.api.PlantImageService
import com.garden.data.api.PlantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    @Named("Plant")
    fun provideApiClient(): Retrofit {
        return ApiClient.getClient()
    }

    @Singleton
    @Provides
    @Named("PlantImage")
    fun providePhotoApiClient(): Retrofit {
        return ApiClient.getPlantPhotoClient()
    }

    @Singleton
    @Provides
    fun providePlantService(@Named("Plant") retrofit: Retrofit): PlantService {
        return PlantService.create(retrofit)
    }

    @Singleton
    @Provides
    fun providePlantImageService(@Named("PlantImage") retrofit: Retrofit): PlantImageService {
        return PlantImageService.create(retrofit)
    }
}
