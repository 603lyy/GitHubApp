package com.bennyhuo.coroutines.library

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * Created by benny on 5/20/17.
 */
fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit): AbstractCoroutine<Unit> {
    return StandaloneCoroutine(context, block)
}



