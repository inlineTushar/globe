package com.globe.platform.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : ViewBinding> Fragment.viewLifecycle(): ReadWriteProperty<Fragment, T> =
    viewLifecycleReadWriteProperty(null)

private fun <T> Fragment.viewLifecycleReadWriteProperty(initializer: (() -> T)?): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {
        private var value: T? = null

        init {
            this@viewLifecycleReadWriteProperty.viewLifecycleOwnerLiveData.observe(
                this@viewLifecycleReadWriteProperty
            ) { viewLifecycleOwner -> viewLifecycleOwner.lifecycle.addObserver(this) }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            value = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            val value = this.value
            if (value != null) return value

            if (thisRef.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                return initializer?.invoke().also { this.value = it }
                    ?: throw IllegalStateException("Called before onCreateView or after onDestroyView.")
            } else {
                throw IllegalStateException("Called before onCreateView or after onDestroyView.")
            }
        }

        override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
            this.value = value
        }
    }
