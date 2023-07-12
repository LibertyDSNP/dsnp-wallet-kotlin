package com.unfinished.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.unfinished.data.api.model.Node

@Entity(tableName = "users")
data class AccountLocal(
    @PrimaryKey val address: String,
    val username: String,
    val publicKey: String,
    val cryptoType: Int,
    val position: Int,
    val networkType: com.unfinished.data.api.model.Node.NetworkType
)
