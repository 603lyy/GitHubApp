package com.lyy.retroapollo

import com.apollographql.apollo.ApolloClient
import com.lyy.retroapollo.utils.Utils
import com.lyy.retroapollo.CallAdapter.Factory
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class RetroApollo private constructor(
    val apolloClient: ApolloClient,
    private val callAdapterFactories: List<Factory>
) {

    //Builder里面的方法
    class Builder {
        private var apolloClient: ApolloClient? = null

        fun apolloClient(apolloClient: ApolloClient): Builder {
            this.apolloClient = apolloClient
            return this
        }

        private val callAdapterFactories = arrayListOf<Factory>(ApolloCallAdapterFactory())

        fun addCallAdapterFactory(callAdapterFactory: Factory): Builder {
            callAdapterFactories.add(callAdapterFactory)
            return this
        }

        fun build() = apolloClient?.let {
            RetroApollo(it, callAdapterFactories)
        } ?: throw IllegalStateException("ApolloClient cannot be null.")
    }

    private val serviceMethodCache = ConcurrentHashMap<Method, ApolloServiceMethod<*>>()

    /**
     * 使用动态代理的方式创建接口interface的代理对象实例
     * 传入的参数是一个接口
     * 返回一个接口的实例
     */
    fun <T : Any> createGraphQLService(serviceClass: KClass<T>): T {
        //判断传入的参数
        Utils.validateServiceInterface(serviceClass.java)

        return Proxy.newProxyInstance(serviceClass.java.classLoader, arrayOf(serviceClass.java),
            object : InvocationHandler {
                override fun invoke(proxy: Any, method: Method, args: Array<Any>?): Any {

                    //判断是否object的Java类里面定义的方法,是则直接invoke
                    if (method.declaringClass == Any::class.java) {
                        return method.invoke(this, args)
                    }

                    return loadServiceMethod(method).invoke(args)
                }
            }) as T
    }

    /**
     * 加锁创建，防止并发访问
     *
     * method是Java的方法
     * 返回的是对接口里方法的封装
     */
    fun loadServiceMethod(method: Method): ApolloServiceMethod<*> {
        var serviceMethod = serviceMethodCache[method]
        if (serviceMethod == null) {
            synchronized(serviceMethodCache) {
                serviceMethod =
                    serviceMethodCache[method] ?: ApolloServiceMethod.Builder(this, method).build()
                        .also {
                            serviceMethodCache[method] = it
                        }
            }
        }
        return serviceMethod!!
    }

    /**
     * as? 为安全的强转，强转失败则返回null
     */
    fun getCallAdapter(type: Type): CallAdapter<Any, Any>? {
        for (callAdapterFactory in callAdapterFactories) {
            val callAdapter = callAdapterFactory.get(type)
            return callAdapter as? CallAdapter<Any, Any> ?: continue
        }
        return null
    }
}