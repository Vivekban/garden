package com.garden.di

import com.garden.data.datasource.local.PlantLocalDataSource
import com.garden.data.datasource.local.RemoteKeysLocalDataSource
import com.garden.data.datasource.local.impl.PlantLocalDataSourceImpl
import com.garden.data.datasource.local.impl.RemoteKeysLocalDataSourceImpl
import com.garden.data.datasource.remote.PlantRemoteDataSource
import com.garden.data.datasource.remote.impl.PlantRemoteDataSourceImpl
import com.garden.data.repository.GardenPlantingRepositoryImpl
import com.garden.data.repository.PlantRepositoryImpl
import com.garden.domain.repository.GardenPlantingRepository
import com.garden.domain.repository.PlantRepository
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
    abstract fun providePlantLocalDataSource(dataSource: PlantLocalDataSourceImpl): PlantLocalDataSource

    @Singleton
    @Binds
    abstract fun provideRemoteKeysLocalDataSource(dataSource: RemoteKeysLocalDataSourceImpl): RemoteKeysLocalDataSource

    @Singleton
    @Binds
    abstract fun providePlantRemoteDataSource(dataSource: PlantRemoteDataSourceImpl): PlantRemoteDataSource

    @Singleton
    @Binds
    abstract fun providePlantRepository(repository: PlantRepositoryImpl): PlantRepository

    @Singleton
    @Binds
    abstract fun provideGardenPlantingRepository(repository: GardenPlantingRepositoryImpl): GardenPlantingRepository

}
