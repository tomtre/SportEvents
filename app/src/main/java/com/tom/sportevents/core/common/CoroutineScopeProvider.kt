package com.tom.sportevents.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

interface CoroutineScopeProvider {
    val applicationScope: CoroutineScope
}

class CoroutineScopeProviderImpl @Inject constructor(
    dispatcherProvider: DispatcherProvider
) : CoroutineScopeProvider {

    override val applicationScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcherProvider.mainImmediate)
}
