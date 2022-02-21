package com.globe.search.extension

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@OptIn(ExperimentalCoroutinesApi::class)
fun SearchView.textChanges(): Flow<String> =
    callbackFlow {
        val textChangeListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                trySend(newText ?: "")
                return true
            }
        }
        val closeListener = SearchView.OnCloseListener {
            trySend("")
            false
        }
        setOnQueryTextListener(textChangeListener)
        setOnCloseListener(closeListener)
        awaitClose {
            setOnQueryTextListener(null)
            setOnQueryTextListener(null)
        }
    }