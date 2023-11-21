package com.garden.data.plant.datasource.local

import com.garden.data.plant.RemoteKeysEntity
import com.garden.data.plant.dao.PlantDao

/**
 * Repository module for handling data operations.
 *
 * Collecting from the Flows in [PlantDao] is main-safe.  Room supports Coroutines and moves the
 * query execution off of the main thread.
 */
interface RemoteKeysLocalDataSource {
    suspend fun insertAll(remoteKeys: List<RemoteKeysEntity>)

    suspend fun remoteKeysRepoId(id: Int): RemoteKeysEntity?

    suspend fun clearAll()
}
