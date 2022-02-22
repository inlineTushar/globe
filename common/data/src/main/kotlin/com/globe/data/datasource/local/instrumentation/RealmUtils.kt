package com.globe.data.datasource.local.instrumentation

import io.realm.Realm
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> executeAndWrapRealmQuery(manager: RealmManager, block: (Realm) -> T): T {
    return withContext(manager.dispatcher) {
        suspendCancellableCoroutine { continuation ->
            try {
                manager.getRealmInstance().use {
                    continuation.resume(block.invoke(it))
                }
            } catch (e: Throwable) {
                Timber.e(e)
                continuation.resumeWithException(e)
            }
        }
    }
}
