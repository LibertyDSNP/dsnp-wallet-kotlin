package com.unfinished.runtime.multiNetwork.runtime.repository

import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.db.model.chain.ChainRuntimeInfoLocal

interface RuntimeVersionsRepository {

    suspend fun getAllRuntimeVersions(): List<RuntimeVersion>
}

internal class DbRuntimeVersionsRepository(
    private val chainDao: ChainDao
) : RuntimeVersionsRepository {

    override suspend fun getAllRuntimeVersions(): List<RuntimeVersion> {
        return chainDao.allRuntimeInfos().map(::mapRuntimeInfoLocalToRuntimeVersion)
    }

    private fun mapRuntimeInfoLocalToRuntimeVersion(runtimeInfoLocal: ChainRuntimeInfoLocal): RuntimeVersion {
        return RuntimeVersion(
            chainId = runtimeInfoLocal.chainId,
            specVersion = runtimeInfoLocal.syncedVersion
        )
    }
}
