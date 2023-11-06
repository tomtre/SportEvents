package com.tom.sportevents.core.network

import com.tom.sportevents.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import timber.log.Timber

@OptIn(ExperimentalSerializationApi::class)
@Suppress("FunctionName")
fun NetworkHttpClient(): HttpClient = HttpClient(CIO) {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Timber.tag("HttpClient").d(message)
            }
        }
        level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
    }
    install(ContentNegotiation) {
        json(
            json = Json {
                encodeDefaults = true
                isLenient = true
                allowSpecialFloatingPointValues = true
                allowStructuredMapKeys = true
                prettyPrint = true
                useArrayPolymorphism = false
                ignoreUnknownKeys = true
                explicitNulls = false
            },
            contentType = ContentType.Application.Json
        )
    }
    defaultRequest {
        contentType(ContentType.Application.Json)
    }
    @Suppress("MagicNumber")
    install(HttpTimeout) {
        requestTimeoutMillis = 5000L
    }
}

