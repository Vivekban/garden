package com.garden.data.plant.datasource.local

import com.garden.data.plant.RemoteKeysEntity
import com.garden.data.plant.dao.RemoteKeysDao
import javax.inject.Inject

/**
 * Local source of Plant
 */
class RemoteKeysLocalDataSourceImpl @Inject constructor(private val dao: RemoteKeysDao) :
    RemoteKeysLocalDataSource {

    override suspend fun insertAll(remoteKeys: List<RemoteKeysEntity>) = dao.insertAll(remoteKeys)

    override suspend fun remoteKeysRepoId(id: Int) = dao.remoteKeysRepoId(id)

    override suspend fun clearAll() = dao.clearAll()
}
