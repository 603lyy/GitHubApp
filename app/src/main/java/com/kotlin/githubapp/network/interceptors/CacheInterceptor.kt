package com.kotlin.githubapp.network.interceptors

import com.kotlin.common.ext.no
import com.kotlin.common.ext.otherwise
import com.kotlin.common.ext.yes
import com.kotlin.common.log.logger
import com.kotlin.githubapp.network.FORCE_NETWORK
import com.kotlin.githubapp.network.Network
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = Network.isAvailable()
            .no {
                request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            .otherwise {
                request.url().queryParameter(FORCE_NETWORK)?.toBoolean()?.let {
                    it.yes {
                        //注意 noCache | noStore，前者不会读缓存；后者既不读缓存，也不对响应进行缓存
                        //尽管看上去 noCache 比较符合我们的需求，但服务端仍然可能返回服务端的缓存
                        //request.newBuilder().cacheControl(CacheControl.Builder().noCache().build()).build()
                        request.newBuilder().cacheControl(
                            CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).build()
                        ).build()
                    }.otherwise {
                        request
                    }
                } ?: request
            }

        request = request.newBuilder()
            .url(request.url().newBuilder().removeAllQueryParameters(FORCE_NETWORK).build()).build()
        return chain.proceed(request).also { response ->
            logger.error("Cache: ${response.cacheResponse()}, Network: ${response.networkResponse()}")
        }
    }
}