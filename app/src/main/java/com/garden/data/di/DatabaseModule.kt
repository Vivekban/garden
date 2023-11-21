package com.garden.data.di

import android.content.Context
import androidx.room.RoomDatabase
import com.garden.data.database.AppDatabase
import com.garden.data.plant.dao.PlantDao
import com.garden.data.plant.dao.PlantingDao
import com.garden.data.plant.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(database: AppDatabase): RoomDatabase = database

    @Provides
    fun providePlantDao(appDatabase: AppDatabase): PlantDao {
        return appDatabase.plantDao()
    }

    @Provides
    fun provideGardenPlantingDao(appDatabase: AppDatabase): PlantingDao {
        return appDatabase.gardenPlantingDao()
    }

    @Provides
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao {
        return appDatabase.remoteKeysDao()
    }
}
