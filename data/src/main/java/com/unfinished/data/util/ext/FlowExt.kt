package com.unfinished.data.util.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformWhile

inline fun <T, R> Flow<List<T>>.mapList(crossinline mapper: suspend (T) -> R) = map { list ->
    list.map { item -> mapper(item) }
}

data class ListDiff<T>(
    val removed: List<T>,
    val addedOrModified: List<T>,
    val all: List<T>
)

fun <T> Flow<List<T>>.diffed(): Flow<ListDiff<T>> {
    return zipWithPrevious().map { (previous, new) ->
        val addedOrModified = new - previous.orEmpty()
        val removed = previous.orEmpty() - new

        ListDiff(removed = removed, addedOrModified = addedOrModified, all = new)
    }
}

fun <T> Flow<T>.zipWithPrevious(): Flow<Pair<T?, T>> = flow {
    var current: T? = null

    collect {
        emit(current to it)

        current = it
    }
}

fun <T> Flow<T>.inBackground() = flowOn(Dispatchers.Default)


inline fun <T> flowOfAll(crossinline producer: suspend () -> Flow<T>): Flow<T> = flow {
    emitAll(producer())
}

fun <T> singleReplaySharedFlow() = MutableSharedFlow<T>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)


/**
 * Similar to [Flow.takeWhile] but emits last element too
 */
fun <T> Flow<T>.takeWhileInclusive(predicate: suspend (T) -> Boolean) = transformWhile {
    emit(it)

    predicate(it)
}