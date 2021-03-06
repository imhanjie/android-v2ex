package com.imhanjie.v2ex.api

import com.imhanjie.v2ex.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiServer {

    private const val REQUEST_TIME_OUT = 15000L
    const val BASE_URL = "https://v2ex.com"

    val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        val logInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
        }
        builder
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(logInterceptor)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(LoginInterceptor())
            .addInterceptor(ParserInterceptor())
            .addInterceptor(CookieInterceptor)
            .connectTimeout(REQUEST_TIME_OUT, TimeUnit.MILLISECONDS)
        builder.build()
    }

    inline fun <reified T> create(): T {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("$BASE_URL/")
            .build()
            .create(T::class.java)
    }

}
