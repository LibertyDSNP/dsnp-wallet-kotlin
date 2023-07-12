package com.unfinished.runtime.di

import android.content.Context
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.unfinished.data.network.NetworkApiCreator
import com.unfinished.data.network.rpc.BulkRetriever
import com.unfinished.data.storage.Preferences
import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.db.dao.StorageDao
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService

interface RuntimeDependencies {

    fun networkApiCreator(): NetworkApiCreator

    fun socketServiceCreator(): SocketService

    fun gson(): Gson

    fun preferences(): Preferences

    fun fileProvider(): FileProvider

    fun context(): Context

    fun storageDao(): StorageDao

    fun bulkRetriever(): BulkRetriever

    fun chainDao(): ChainDao
}
