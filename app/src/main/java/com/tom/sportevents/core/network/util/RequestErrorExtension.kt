package com.tom.sportevents.core.network.util

val <T> RequestError<T>.response: T?
    get() = (this as? RequestError.Api<T>)?.response

val <T> RequestError<T>.exception: Throwable?
    get() = (this as? RequestError.Exception)?.throwable
