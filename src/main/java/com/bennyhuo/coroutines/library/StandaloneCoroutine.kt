package com.bennyhuo.coroutines.library

import kotlin.coroutines.CoroutineContext

class StandaloneCoroutine<T>(context: CoroutineContext, block: suspend () -> T): AbstractCoroutine<T>(context, block)