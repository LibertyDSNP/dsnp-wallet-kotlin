package com.unfinished.runtime.di

import android.content.Context
import com.google.gson.Gson
import com.unfinished.common.data.network.NetworkApiCreator
import com.unfinished.common.data.network.rpc.BulkRetriever
import com.unfinished.common.data.storage.Preferences
import com.unfinished.common.interfaces.FileProvider
import com.unfinished.core.db.dao.ChainDao
import com.unfinished.core.db.dao.StorageDao
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
