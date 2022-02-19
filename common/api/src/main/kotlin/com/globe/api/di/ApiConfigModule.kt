package com.globe.api.di

import com.globe.api.BuildConfig
import com.squareup.moshi.Moshi
import com.globe.api.instrumentation.MoshiLocalDateAdapter
import com.globe.api.instrumentation.MoshiLocalDateTimeAdapter
import com.globe.api.instrumentation.MoshiLocalTimeAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

val apiConfigModule = module {
    single {
        Moshi.Builder()
            .add(LocalDate::class.java, MoshiLocalDateAdapter().nullSafe())
            .add(LocalTime::class.java, MoshiLocalTimeAdapter().nullSafe())
            .add(LocalDateTime::class.java, MoshiLocalDateTimeAdapter().nullSafe())
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
