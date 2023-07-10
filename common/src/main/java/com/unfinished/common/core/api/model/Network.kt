package com.unfinished.common.core.api.model

data class Network(
    val type: Node.NetworkType
) {
    val name = type.readableName
}
