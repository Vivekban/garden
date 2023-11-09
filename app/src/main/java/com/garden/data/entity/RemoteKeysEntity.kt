package com.garden.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [RemoteKeysEntity] used to track information loaded from network so that more items can be fetched
 * accordingly.
 */
@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "p_key")
    val previousKey: Int?,
    @ColumnInfo(name = "n_key")
    val nextKey: Int?
)

