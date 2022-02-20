package com.globe

import android.app.Application
import com.globe.countrylist.di.countryListModule
import com.globe.data.di.dataComponent
import com.globe.di.domainComponent
import com.globe.di.homeModule
import com.globe.platform.APPLICATION_BG
import com.globe.platform.APPLICATION_MAIN
import com.globe.search.di.searchModule
import com.tushar.countrydetail.di.countryDetailModule
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GlobeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        setupThreadingContexts()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@GlobeApp)
            modules(dataComponent)
            modules(homeModule)
            modules(domainComponent)
            modules(countryListModule)
            modules(searchModule)
            modules(countryDetailModule)
        }
    }

    private fun setupThreadingContexts() {
        APPLICATION_MAIN = Dispatchers.Main + CoroutineExceptionHandler { _, error -> throw error }
        APPLICATION_BG = Dispatchers.Default + CoroutineExceptionHandler { _, error -> throw error }
    }
}
