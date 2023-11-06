package com.tom.sportevents.core.network.util

import arrow.core.left
import arrow.core.right
import com.tom.sportevents.core.common.runSuspendCatching
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import timber.log.Timber

internal suspend inline fun <reified RES : Any, reified ERR : Any, reified REQ : Any> HttpClient.put(
    url: String,
    body: REQ,
    builder: HttpRequestBuilder.() -> Unit = {}
): Response<ERR, RES> =
    request {
        put(url) {
            setBody(body)
            builder()
        }
    }

internal suspend inline fun <reified RES : Any, reified ERR : Any, reified REQ : Any> HttpClient.patch(
    url: String,
    body: REQ,
    builder: HttpRequestBuilder.() -> Unit = {}
): Response<ERR, RES> =
    request {
        patch(url) {
            setBody(body)
            builder()
        }
    }

internal suspend inline fun <reified RES : Any, reified ERR : Any> HttpClient.delete(
    url: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): Response<ERR, RES> =
    request {
        delete(url) {
            builder()
        }
    }

internal suspend inline fun <reified RES : Any, reified ERR : Any> HttpClient.get(
    url: String,
    builder: HttpRequestBuilder.() -> Unit = {}
): Response<ERR, RES> =
    request {
        get(url) {
            builder()
        }
    }

internal suspend inline fun <reified RES : Any, reified ERR : Any, reified REQ : Any> HttpClient.post(
    url: String,
    body: REQ,
    builder: HttpRequestBuilder.() -> Unit = {}
): Response<ERR, RES> =
    request {
        post(url) {
            setBody(body)
            builder()
        }
    }

internal suspend inline fun <reified RES : Any, reified ERR : Any> request(
    request: () -> HttpResponse
): Response<ERR, RES> =
    runSuspendCatching { request() }
        .fold(
            onSuccess = { response ->
                when (response.status.value) {
                    in 200..299 -> runSuspendCatching { response.body<RES>() }
                        .fold(
                            onSuccess = { it.right() },
                            onFailure = {
                                Timber.e("Error while parsing success response body: ${it.message.orEmpty()}")
                                RequestError.Exception(it).left()
                            }
                        )

                    else -> runSuspendCatching { response.body<ERR>() }
                        .fold(
                            onSuccess = { RequestError.Api(response.status.value, it) },
                            onFailure = {
                                Timber.e("Error while parsing error response body: ${it.message.orEmpty()}")
                                RequestError.Exception(it)
                            }
                        )
                        .left()
                }
            },
            onFailure = {
                Timber.e("Request error: ${it.message.orEmpty()}")
                RequestError.Exception(it).left()
            }
        )
