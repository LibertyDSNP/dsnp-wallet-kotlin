package com.unfinished.common.utils.formatting

import com.unfinished.common.resources.ResourceManager

interface Formatable {

    fun format(resourceManager: ResourceManager): String?
}
