package com.garden.data.di

import com.garden.data.api.ApiClient
import com.garden.data.plant.PlantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    @Named("Plant")
    fun provideApiClient(): Retrofit {
        return ApiClient.getClient()
    }

    @Singleton
    @Provides
    fun providePlantService(@Named("Plant") retrofit: Retrofit): PlantService {
        return PlantService.create(retrofit)
    }
}
