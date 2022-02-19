package com.globe.api.di

import com.globe.api.BuildConfig
import com.globe.api.instrumentation.Currency
import com.globe.api.instrumentation.CurrencyJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val apiConfigModule = module {
    single {
        Moshi.Builder()
            .add(
                Types.newParameterizedType(List::class.java, Currency::class.java),
                CurrencyJsonAdapter()
            )
            .build()
    }

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = when (BuildConfig.DEBUG) {
            true -> HttpLoggingInterceptor.Level.BODY
            false -> HttpLoggingInterceptor.Level.NONE
        }
        interceptor
    }

    single {
        listOf(get<HttpLoggingInterceptor>())
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(buildOkHttpClient(interceptors = get()))
    }
}

private fun buildOkHttpClient(
    interceptors: List<Interceptor>,
    timeout: Long = 10,
): OkHttpClient {
    val builder = OkHttpClient.Builder()
    for (interceptor in interceptors) {
        builder.addInterceptor(interceptor)
    }
    builder.readTimeout(timeout, TimeUnit.SECONDS)
    return builder.build()
}
