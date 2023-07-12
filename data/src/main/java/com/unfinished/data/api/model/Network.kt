package com.unfinished.data.api.model

data class Network(
    val type: Node.NetworkType
) {
    val name = type.readableName
}
