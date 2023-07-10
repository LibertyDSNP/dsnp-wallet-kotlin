package com.unfinished.common.utils

fun Iterable<String>.asQueryParam() = joinToString(separator = ",")
