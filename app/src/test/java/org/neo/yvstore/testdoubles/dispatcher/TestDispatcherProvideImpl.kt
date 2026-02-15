package org.neo.yvstore.testdoubles.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import org.neo.yvstore.core.common.dispatcher.DispatcherProvider

class TestDispatcherProvider(
    private val testDispatcher: CoroutineDispatcher,
) : DispatcherProvider {
    override val main: CoroutineDispatcher = testDispatcher
    override val io: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
}