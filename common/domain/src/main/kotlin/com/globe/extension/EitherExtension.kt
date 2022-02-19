package com.globe.extension

import arrow.core.Either

fun <T, Y : Throwable> Either<Y, T>.onFailure(block: (Y) -> Unit): Either<Y, T> {
    fold({ block(it) }, {})
    return this
}

fun <T, Y : Throwable> Either<Y, T>.onSuccess(block: (T) -> Unit): Either<Y, T> {
    fold({}, { block(it) })
    return this
}
