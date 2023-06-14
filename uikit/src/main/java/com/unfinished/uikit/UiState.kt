package com.unfinished.uikit

interface UiState<T> {
    class Loading<T> : UiState<T>
    data class DataLoaded<T>(val data: T) : UiState<T>
    data class Error<T>(val throwable: Throwable) : UiState<T>
}

fun <T> UiState<T>.asDataLoaded(): UiState.DataLoaded<T>? = this as? UiState.DataLoaded<T>
fun <T> T.toDataLoaded() = UiState.DataLoaded(this)
fun <T> Throwable.toError() = UiState.Error<T>(this).also { it.throwable.printStackTrace() }
