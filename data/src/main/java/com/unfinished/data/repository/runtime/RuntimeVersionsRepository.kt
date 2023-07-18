package com.unfinished.data.repository.runtime

import com.unfinished.data.db.dao.ChainDao
import com.unfinished.data.db.model.chain.ChainRuntimeInfoLocal
import com.unfinished.data.model.RuntimeVersion

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
