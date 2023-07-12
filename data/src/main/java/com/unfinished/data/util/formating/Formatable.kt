package com.unfinished.data.util.formating

import com.unfinished.data.util.resource.ResourceManager

interface Formatable {

    fun format(resourceManager: ResourceManager): String?
}
