package com.globe.unittestingtools

import io.mockk.mockk
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import timber.log.Timber

class TimberExtension : BeforeAllCallback, AfterAllCallback {

    private val mockTree: Timber.Tree = mockk(relaxed = true)

    override fun beforeAll(context: ExtensionContext?) {
        Timber.plant(mockTree)
    }

    override fun afterAll(context: ExtensionContext?) {
        Timber.uproot(mockTree)
    }
}
