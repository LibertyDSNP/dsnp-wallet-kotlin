package com.unfinished.common.di

import coil.ImageLoader
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.common.resources.ContextManager
import com.unfinished.data.network.rpc.SocketSingleRequestExecutor

interface CommonApi {
    fun imageLoader(): ImageLoader
    fun provideClipboardManager(): ClipboardManager
    fun contextManager(): ContextManager
}
