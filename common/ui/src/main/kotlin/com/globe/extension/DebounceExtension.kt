package com.globe.extension

import android.os.SystemClock
import android.view.View

const val DEBOUNCE_DELAY = 250L

inline fun View.onClickDebounced(crossinline block: () -> Unit) {
    var lastClickTime = 0L
    // https://stackoverflow.com/questions/5608720/android-preventing-double-click-on-a-button/9950832
    setOnClickListener {
        val delta = SystemClock.elapsedRealtime() - lastClickTime
        if (delta > DEBOUNCE_DELAY) {
            lastClickTime = SystemClock.elapsedRealtime()
            block.invoke()
        }
    }
}
