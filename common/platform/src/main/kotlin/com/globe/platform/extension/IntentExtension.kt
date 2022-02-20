package com.globe.platform.extension

import android.content.Context
import android.content.Intent

fun internalIntent(context: Context, action: String): Intent =
    Intent(action).setPackage(context.packageName)
