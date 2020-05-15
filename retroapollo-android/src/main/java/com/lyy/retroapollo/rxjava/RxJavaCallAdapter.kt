package com.lyy.retroapollo.rxjava

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.lyy.retroapollo.CallAdapter
import com.lyy.retroapollo.rxjava.RxReturnType.OBSERVABLE
import com.lyy.retroapollo.rxjava.RxReturnType.SINGLE
import com.lyy.retroapollo.utils.Utils
import rx.Observable
import rx.Scheduler
import rx.Single
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by benny on 8/5/17.
 */
enum class RxReturnType {
    OBSERVABLE, SINGLE
}

class RxJavaCallAdapter<T>(
    val rxReturnType: RxReturnType,
    val dataType: Type,
    val subscribeScheduler: Scheduler? = null,
    val observableScheduler: Scheduler? = null
) : CallAdapter<T, Any> {
    override fun responseType(): Type {
        return if (dataType is ParameterizedType) {
            Utils.getParameterUpperBound(0, dataType)
        } else {
            dataType
        }
    }

    /**
     * 传入的是ApolloCall
     * 转换返回rxjava的call
     */
    override fun adapt(call: ApolloCall<T>): Any {
        //对call进行封装，包装成集继承OnSubscribe的CallExecuteOnSubscribe类型
        val callFunc = CallExecuteOnSubscribe(call)

        //callFunc进行转换
        var originalObservable = Observable.create(callFunc)

        originalObservable =
            subscribeScheduler?.let(originalObservable::subscribeOn) ?:originalObservable

        originalObservable =
            observableScheduler?.let(originalObservable::observeOn) ?: originalObservable

        val observable: Observable<*> =
            // Observable<Response<Data>>
            if (dataType is ParameterizedType) {
                originalObservable
            } else {
                originalObservable.map { it.data() }
            }

        return when (rxReturnType) {
            OBSERVABLE -> observable
            SINGLE -> observable.toSingle()
        }
    }
}

class RxJavaCallAdapterFactory : CallAdapter.Factory() {

    private var subscribeScheduler: Scheduler? = null

    fun subscribeScheduler(scheduler: Scheduler): RxJavaCallAdapterFactory {
        subscribeScheduler = scheduler
        return this
    }

    private var observableScheduler: Scheduler? = null

    fun observableScheduler(scheduler: Scheduler): RxJavaCallAdapterFactory {
        observableScheduler = scheduler
        return this
    }

    override fun get(returnType: Type): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        val dataType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (dataType is ParameterizedType) {
            val responseType = getRawType(dataType)
            if (responseType != Response::class.java) {
                return null
            }
        }
        val rxReturnType = when (rawType) {
            Single::class.java -> SINGLE
            Observable::class.java -> OBSERVABLE
            else -> null
        } ?: return null
        return RxJavaCallAdapter<Any>(
            rxReturnType,
            dataType,
            subscribeScheduler,
            observableScheduler
        )
    }
}