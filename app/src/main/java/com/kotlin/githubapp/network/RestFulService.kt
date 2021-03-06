package com.kotlin.githubapp.network

import com.kotlin.common.ext.ensureDir
import com.kotlin.githubapp.AppContext
import com.kotlin.githubapp.network.compat.enableTls12OnPreLollipop
import com.kotlin.githubapp.network.interceptors.AcceptInterceptor
import com.kotlin.githubapp.network.interceptors.AuthInterceptor
import com.kotlin.githubapp.network.interceptors.CacheInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory2
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.github.com"

//通过一个 QueryParameter 让 CacheInterceptor 添加 no-cache
const val FORCE_NETWORK = "forceNetwork"

private val cacheFile by lazy {
    File(AppContext.cacheDir, "webServiceApi").apply { ensureDir() }
}

val retrofit by lazy {
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(
            RxJavaCallAdapterFactory2.createWithSchedulers(
                Schedulers.io(),
                AndroidSchedulers.mainThread()
            )
        )
        .client(
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.MINUTES)
                .cache(Cache(cacheFile, 1024 * 1024 * 1024))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(AcceptInterceptor())
                .addInterceptor(CacheInterceptor())
                .addInterceptor(AuthInterceptor())
                .enableTls12OnPreLollipop()
                .build()
        )
        .baseUrl(BASE_URL)
        .build()
}