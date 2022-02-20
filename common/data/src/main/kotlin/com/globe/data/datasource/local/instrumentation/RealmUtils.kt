package com.globe.data.datasource.local.instrumentation

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
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

@ExperimentalCoroutinesApi
fun <T : RealmModel> flowAllWrapRealm(manager: RealmManager, block: (Realm) -> RealmQuery<T>): Flow<List<T>> {
    return callbackFlow<List<T>> {
        manager.getRealmInstance().use { realm ->
            val results = block(realm).findAllAsync()
            results.addChangeListener { result ->
                runCatching { offer(realm.copyFromRealm(result)) }
            }
            awaitClose {
                results.removeAllChangeListeners()
            }
        }
    }.flowOn(manager.dispatcher)
}
