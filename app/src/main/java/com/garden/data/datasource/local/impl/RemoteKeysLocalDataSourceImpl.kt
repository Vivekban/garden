package com.garden.data.datasource.local.impl

import com.garden.data.database.dao.RemoteKeysDao
import com.garden.data.datasource.local.RemoteKeysLocalDataSource
import com.garden.data.entity.RemoteKeysEntity
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
