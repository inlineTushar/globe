package com.globe.platform

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope, KoinComponent {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext get() = Dispatchers.IO + job

    protected fun coroutineWrapper(command: suspend () -> Unit) {
        CoroutineScope(coroutineContext).launch {
            command.invoke()
        }
    }
}
