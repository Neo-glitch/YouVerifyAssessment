package org.neo.yvstore.core.network.utils

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

suspend fun <T> Task<T>.awaitWithTimeout(
    timeout: Long = 20,
    unit: TimeUnit = TimeUnit.SECONDS,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): T = withContext(dispatcher) {
    Tasks.await(this@awaitWithTimeout, timeout, unit)
}
