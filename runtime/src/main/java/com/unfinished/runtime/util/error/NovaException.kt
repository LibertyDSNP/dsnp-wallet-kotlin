package com.unfinished.runtime.util.error

import com.unfinished.runtime.util.resource.ResourceManager

class NovaException(
    val kind: Kind,
    message: String?,
    exception: Throwable? = null,
) : RuntimeException(message, exception) {

    enum class Kind {
        NETWORK,
        UNEXPECTED
    }

    companion object {

        fun networkError(resourceManager: ResourceManager, throwable: Throwable): NovaException {
            return NovaException(Kind.NETWORK, "", throwable) // TODO: add common error text to resources
        }

        fun unexpectedError(exception: Throwable): NovaException {
            return NovaException(Kind.UNEXPECTED, exception.message ?: "", exception) // TODO: add common error text to resources
        }
    }
}
