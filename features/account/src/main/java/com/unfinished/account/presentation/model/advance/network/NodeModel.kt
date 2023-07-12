package com.unfinished.account.presentation.model.advance.network

data class NodeModel(
    val id: Int,
    val name: String,
    val link: String,
    val networkModelType: NetworkModel.NetworkTypeUI,
    val isDefault: Boolean,
    val isActive: Boolean
)
