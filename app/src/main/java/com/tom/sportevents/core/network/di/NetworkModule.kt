package com.tom.sportevents.core.network.di

import com.tom.sportevents.BuildConfig
import com.tom.sportevents.core.network.NetworkDataSource
import com.tom.sportevents.core.network.NetworkDataSourceImpl
import com.tom.sportevents.core.network.NetworkHttpClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [NetworkModule.Bindings::class])
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named(ApiUrl)
    fun provideApiUrl(): String =
        BuildConfig.API_URL

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient =
        NetworkHttpClient()

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class Bindings {

        @Singleton
        @Binds
        abstract fun bindNetworkDataSource(networkDataSourceImpl: NetworkDataSourceImpl): NetworkDataSource
    }
}
