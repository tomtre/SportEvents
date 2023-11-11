package com.tom.sportevents.core.common

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlin.coroutines.cancellation.CancellationException

@Suppress("TooGenericExceptionCaught")
suspend inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

// It works like StateFlow with "no value" option and without distinctUntilChanged option
// Equivalent of BehaviorSubject / BehaviorRelay from the RxJava world
@Suppress("FunctionName")
fun <T> MutableBehaviorFlow(): MutableSharedFlow<T> =
    MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

// It works like StateFlow with "no value" option and without distinctUntilChanged option.
// Equivalent of BehaviorSubject / BehaviorRelay from the RxJava world
@Suppress("FunctionName")
fun <T> MutableBehaviorFlow(default: T): MutableSharedFlow<T> =
    MutableSharedFlow<T>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .apply { tryEmit(default) }
