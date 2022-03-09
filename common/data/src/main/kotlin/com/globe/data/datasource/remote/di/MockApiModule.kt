package com.globe.data.datasource.remote.di

import android.content.Context
import co.infinum.retromock.Retromock
import com.globe.data.datasource.remote.api.CountryMockApiService
import org.koin.dsl.module
import retrofit2.Retrofit

val mockApiModule = module {
    single {
        val builder: Retrofit.Builder = get()
        Retromock.Builder()
            .retrofit(builder.baseUrl(COUNTRY_API).build())
            .defaultBodyFactory(get<Context>().assets::open)
            .build()
    }

    single<CountryMockApiService> {
        get<Retromock>().create(CountryMockApiService::class.java)
    }
}
