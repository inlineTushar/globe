package com.globe.unittestingtools

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.globe.platform.APPLICATION_BG
import com.globe.platform.APPLICATION_MAIN
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.*

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineScopeExtension constructor(private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    BeforeEachCallback, AfterEachCallback, AfterAllCallback,
    TestCoroutineScope by TestCoroutineScope(dispatcher), ParameterResolver {

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
        APPLICATION_MAIN = dispatcher
        APPLICATION_BG = dispatcher
        ArchTaskExecutor.getInstance()
            .setDelegate(
                object : TaskExecutor() {
                    override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
                    override fun postToMainThread(runnable: Runnable) = runnable.run()
                    override fun isMainThread(): Boolean = true
                }
            )
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    override fun afterAll(context: ExtensionContext?) {
        unmockkAll()
    }

    override fun supportsParameter(
        parameterContext: ParameterContext?,
        extensionContext: ExtensionContext?
    ) = parameterContext?.parameter?.parameterizedType == TestCoroutineDispatcher::class.java

    override fun resolveParameter(
        parameterContext: ParameterContext?,
        extensionContext: ExtensionContext?
    ) = dispatcher
}
