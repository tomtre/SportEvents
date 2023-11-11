package com.tom.sportevents.core.common.di

import com.tom.sportevents.core.common.CoroutineScopeProvider
import com.tom.sportevents.core.common.CoroutineScopeProviderImpl
import com.tom.sportevents.core.common.DispatcherProvider
import com.tom.sportevents.core.common.DispatcherProviderImpl
import com.tom.sportevents.core.common.time.eventsource.TimeModificationEventSource
import com.tom.sportevents.core.common.time.eventsource.TimeModificationEventSourceImpl
import com.tom.sportevents.core.common.time.formater.BasicDateFormatter
import com.tom.sportevents.core.common.time.formater.DateFormatter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Singleton
    @Binds
    abstract fun bindDispatcherProvider(dispatcherProviderImpl: DispatcherProviderImpl): DispatcherProvider

    @Singleton
    @Binds
    abstract fun bindCoroutineScopeProvider(coroutineScopeProviderImpl: CoroutineScopeProviderImpl): CoroutineScopeProvider

    @Singleton
    @Binds
    abstract fun bindTimeModificationEventSource(
        timeModificationEventSourceImpl: TimeModificationEventSourceImpl
    ): TimeModificationEventSource

    @Singleton
    @Binds
    abstract fun bindDateFormatter(basicDateFormatter: BasicDateFormatter): DateFormatter
}
