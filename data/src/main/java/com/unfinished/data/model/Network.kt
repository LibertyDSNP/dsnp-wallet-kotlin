package com.unfinished.data.model

data class Network(
    val type: Node.NetworkType
) {
    val name = type.readableName
}
