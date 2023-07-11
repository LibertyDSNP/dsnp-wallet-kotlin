package com.unfinished.core.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unfinished.common.core.api.model.Node

@Entity(tableName = "users")
data class AccountLocal(
    @PrimaryKey val address: String,
    val username: String,
    val publicKey: String,
    val cryptoType: Int,
    val position: Int,
    val networkType: com.unfinished.common.core.api.model.Node.NetworkType
)
