package com.unfinished.common.mixin.hints

import kotlinx.coroutines.flow.Flow

interface HintsMixin {

    val hintsFlow: Flow<List<CharSequence>>
}
