package com.garden.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.garden.common.Constant
import com.garden.common.Constant.DATABASE_NAME
import com.garden.common.Constant.PLANT_DATA_FILENAME
import com.garden.data.database.dao.PlantDao
import com.garden.data.database.dao.PlantingDao
import com.garden.data.database.dao.RemoteKeysDao
import com.garden.data.database.workers.SeedDatabaseWorker
import com.garden.data.database.workers.SeedDatabaseWorker.Companion.KEY_FILENAME
import com.garden.data.entity.PlantEntity
import com.garden.data.entity.PlantingEntity
import com.garden.data.entity.RemoteKeysEntity

/**
 * The Room database for this app
 */
@Database(
    entities = [PlantingEntity::class, PlantEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenPlantingDao(): PlantingDao
    abstract fun plantDao(): PlantDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            if (Constant.LOAD_DEFAULT_PLANT_IN_DB) {
                                val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                                    .setInputData(workDataOf(KEY_FILENAME to PLANT_DATA_FILENAME))
                                    .build()
                                WorkManager.getInstance(context).enqueue(request)
                            }
                        }
                    }
                )
                .build()
        }
    }
}
