package com.tom.sportevents.core.network.util

import arrow.core.Either

typealias Response<ERR, RES> = Either<RequestError<out ERR>, RES>
