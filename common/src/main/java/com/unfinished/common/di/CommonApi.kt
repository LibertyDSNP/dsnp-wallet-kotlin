package com.unfinished.common.di

import coil.ImageLoader
import com.unfinished.common.data.network.rpc.SocketSingleRequestExecutor
import com.unfinished.common.resources.ClipboardManager
import com.unfinished.common.resources.ContextManager

interface CommonApi {
    fun imageLoader(): ImageLoader
    fun provideClipboardManager(): ClipboardManager
    fun contextManager(): ContextManager
    fun socketSingleRequestExecutor(): SocketSingleRequestExecutor
}
