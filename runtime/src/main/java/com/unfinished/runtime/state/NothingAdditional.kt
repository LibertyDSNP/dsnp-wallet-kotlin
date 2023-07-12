package com.unfinished.runtime.state

import com.unfinished.data.util.resource.ResourceManager
import com.unfinished.runtime.multiNetwork.chain.model.Chain

object NothingAdditional : AssetSharedStateAdditionalData {

    override val identifier: String = "Nothing"

    override fun format(resourceManager: ResourceManager): String? {
        return null
    }
}

fun uniqueOption(valid: (Chain, Chain.Asset) -> Boolean): SupportedOptionsResolver<NothingAdditional> {
    return { chain, asset ->
        if (valid(chain, asset)) listOf(NothingAdditional) else emptyList()
    }
}
