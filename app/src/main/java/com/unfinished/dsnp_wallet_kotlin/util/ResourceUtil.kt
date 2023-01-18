package com.unfinished.dsnp_wallet_kotlin.util


sealed class UiStateResource<out T> {
    object Loading : UiStateResource<Nothing>()
    data class Success<out T>(val data: T) : UiStateResource<T>(){
        var hasBeenHandled = false
            private set // Allow external read but not write

        /**
         * Returns the content and prevents its use again.
         */
        fun getContentIfNotHandled(content: (T) -> Unit) {
            if (!hasBeenHandled) {
                content.invoke(data)
                hasBeenHandled = true
            }
        }

        /**
         * Returns the content, even if it's already been handled.
         */
        fun peekContent(): T = data
    }
    data class Failure(val error: Error) : UiStateResource<Nothing>()
}