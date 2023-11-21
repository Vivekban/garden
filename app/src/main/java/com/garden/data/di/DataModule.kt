package com.garden.data.di

import com.garden.data.plant.PlantRepositoryImpl
import com.garden.data.plant.datasource.local.PlantLocalDataSource
import com.garden.data.plant.datasource.local.PlantLocalDataSourceImpl
import com.garden.data.plant.datasource.local.RemoteKeysLocalDataSource
import com.garden.data.plant.datasource.local.RemoteKeysLocalDataSourceImpl
import com.garden.data.plant.datasource.remote.PlantRemoteDataSource
import com.garden.data.plant.datasource.remote.PlantRemoteDataSourceImpl
import com.garden.data.planting.PlantingRepositoryImpl
import com.garden.data.planting.datasource.local.PlantingLocalDataSource
import com.garden.data.planting.datasource.local.PlantingLocalDataSourceImpl
import com.garden.domain.plant.PlantRepository
import com.garden.domain.planting.GardenPlantingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun providePlantLocalDataSource(dataSource: PlantLocalDataSourceImpl):
        PlantLocalDataSource

    @Singleton
    @Binds
    abstract fun provideRemoteKeysLocalDataSource(dataSource: RemoteKeysLocalDataSourceImpl):
        RemoteKeysLocalDataSource

    @Singleton
    @Binds
    abstract fun providePlantRemoteDataSource(dataSource: PlantRemoteDataSourceImpl):
        PlantRemoteDataSource

    @Singleton
    @Binds
    abstract fun providePlantingRemoteDataSource(dataSource: PlantingLocalDataSourceImpl):
        PlantingLocalDataSource

    @Singleton
    @Binds
    abstract fun providePlantRepository(repository: PlantRepositoryImpl): PlantRepository

    @Singleton
    @Binds
    abstract fun provideGardenPlantingRepository(repository: PlantingRepositoryImpl):
        GardenPlantingRepository
}
