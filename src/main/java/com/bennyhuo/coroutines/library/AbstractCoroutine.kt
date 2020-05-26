package com.bennyhuo.coroutines.library

import com.bennyhuo.coroutines.utils.log
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.*

typealias OnComplete<T> = (T?, Throwable?) -> Unit

sealed class State {
    object InComplete : State()
    class Complete<T>(val value: T? = null, val exception: Throwable? = null) : State()
    class CompleteHandler<T>(val handler: OnComplete<T>) : State()
}

abstract class AbstractCoroutine<T>(
    override val context: CoroutineContext,
    block: suspend () -> T
) : Continuation<T> {

    protected val state = AtomicReference<State>()

    init {
        state.set(State.InComplete)

        //类似于设置callback
        block.startCoroutine(this)
    }

    val isCompleted
        get() = state.get() is State.Complete<*>

    override fun resumeWith(result: Result<T>) {
        result.fold({ value ->
            when (val currentState = state.getAndSet(State.Complete(value))) {
                is State.CompleteHandler<*> -> {
                    (currentState as State.CompleteHandler<T>).handler(value as T, null)
                }
            }
        }, { exception ->
            val currentState = state.getAndSet(State.Complete<T>(null, exception))
            when (currentState) {
                is State.CompleteHandler<*> -> {
                    (currentState as State.CompleteHandler<T>).handler(null, exception)
                }
            }
        })
    }

    suspend fun join() {
        when (val currentState = state.get()) {
            is State.InComplete -> return joinSuspend()
            is State.Complete<*> ->
                when (currentState.value) {
                    null -> throw currentState.exception!!
                    else -> return
                }
            else -> throw IllegalStateException("Invalid State: $currentState")
        }
    }

    private suspend fun joinSuspend() = suspendCoroutine<Unit> { continuation ->
        doOnCompleted { t, throwable ->
            when {
                t != null -> continuation.resume(Unit)
                throwable != null -> continuation.resumeWithException(throwable)
                else -> throw IllegalStateException("Won't happen.")
            }
        }
    }

    private fun doOnCompleted(block: (T?, Throwable?) -> Unit) {
        if (!state.compareAndSet(State.InComplete, State.CompleteHandler<T>(block))) {
            when (val currentState = state.get()) {
                is State.Complete<*> -> {
                    (currentState as State.Complete<T>).let {
                        block(currentState.value, currentState.exception)
                    }
                }
                else -> throw IllegalStateException("Invalid State: $currentState")
            }
        }
    }
}