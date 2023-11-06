package com.tom.sportevents.core.network.util

sealed interface RequestError<T> {

    data class Api<T>(
        val code: Int,
        val response: T
    ) : RequestError<T>

    data class Exception(
        val throwable: Throwable
    ) : RequestError<Nothing>
}
