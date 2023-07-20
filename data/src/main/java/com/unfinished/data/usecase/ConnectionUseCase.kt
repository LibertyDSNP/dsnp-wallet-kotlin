package com.unfinished.data.usecase

import com.unfinished.data.multiNetwork.ChainRegistry
import com.unfinished.data.multiNetwork.connection.ChainConnectionFactory
import com.unfinished.data.multiNetwork.connection.ConnectionPool
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionUseCase @Inject constructor(
    private val connectionPool: ConnectionPool,
    private val chainUseCase: ChainUseCase
){

    fun setUpNewConnection(chainUrl: String) = runBlocking {
        chainUseCase.getChain().first().copy().apply {
            nodes = this.nodes.map { it.copy(url = chainUrl) }
        }.let {
            connectionPool.setupConnection(it)
        }
    }
}