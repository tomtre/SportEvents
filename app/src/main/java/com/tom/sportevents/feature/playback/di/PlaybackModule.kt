package com.tom.sportevents.feature.playback.di

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PlaybackModule {

    @Provides
    @ViewModelScoped
    fun providePlayer(@ApplicationContext context: Context): Player =
        ExoPlayer.Builder(context).build()
}