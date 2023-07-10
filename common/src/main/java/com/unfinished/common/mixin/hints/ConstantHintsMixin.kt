package com.unfinished.common.mixin.hints

import com.unfinished.common.utils.WithCoroutineScopeExtensions
import com.unfinished.common.utils.flowOf
import com.unfinished.common.utils.inBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

abstract class ConstantHintsMixin(
    coroutineScope: CoroutineScope
) : HintsMixin,
    CoroutineScope by coroutineScope,
    WithCoroutineScopeExtensions by WithCoroutineScopeExtensions(coroutineScope) {

    abstract suspend fun getHints(): List<String>

    override val hintsFlow: Flow<List<String>> = flowOf {
        getHints()
    }
        .inBackground()
        .shareLazily()
}
