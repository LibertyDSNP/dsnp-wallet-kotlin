package com.unfinished.common.data.network

import com.unfinished.common.R
import com.unfinished.common.base.BaseException
import com.unfinished.common.resources.ResourceManager
import retrofit2.HttpException
import java.io.IOException

class HttpExceptionHandler(
    private val resourceManager: ResourceManager
) {
    suspend fun <T> wrap(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: Throwable) {
            throw transformException(e)
        }
    }

    fun transformException(exception: Throwable): BaseException {
        return when (exception) {
            is HttpException -> {
                val response = exception.response()!!

                val errorCode = response.code()
                response.errorBody()?.close()

                BaseException.httpError(errorCode, resourceManager.getString(R.string.common_undefined_error_message))
            }
            is IOException -> BaseException.networkError(resourceManager.getString(R.string.connection_error_message_v2_2_0), exception)
            else -> BaseException.unexpectedError(exception)
        }
    }
}
