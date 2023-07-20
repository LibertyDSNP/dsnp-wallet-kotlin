package com.unfinished.data.usecase

import com.unfinished.data.multiNetwork.ChainRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChainUseCase @Inject constructor(
    private val chainRegistry: ChainRegistry,
){

    suspend fun getChain() = chainRegistry.currentChains.first()
}