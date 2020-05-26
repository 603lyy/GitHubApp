package com.bennyhuo.coroutines.library

import kotlin.coroutines.CoroutineContext


/**
 * Created by benny on 5/20/17.
 */
fun launch(context: CoroutineContext = CommonPoolContext, block: suspend () -> Unit): AbstractCoroutine<Unit> {
    return StandaloneCoroutine(context, block)
}



