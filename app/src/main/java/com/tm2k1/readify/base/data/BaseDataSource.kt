package com.tm2k1.readify.base.data

import com.tm2k1.readify.utils.DataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseDataSource {
    protected suspend fun <R> withResultContext(
        context: CoroutineContext = Dispatchers.IO,
        requestBlock: suspend CoroutineScope.() -> R
    ): DataResult<R> = withContext(context) {
        return@withContext try {
            val response = requestBlock()
            DataResult.Success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            DataResult.Error(e)
        }
    }
}

