package com.bennyhuo.coroutines.library

import java.util.concurrent.LinkedBlockingDeque
import kotlin.coroutines.CoroutineContext

typealias EventTask = () -> Unit

class BlockingQueueDispatcher : LinkedBlockingDeque<EventTask>(), Dispatcher {

    override fun dispatch(block: EventTask) {
        offer(block)
    }
}

/**
 * 如果eventQueue是空的，它的take方法会阻塞
 */
class BlockingCoroutine<T>(
    context: CoroutineContext,
    private val eventQueue: LinkedBlockingDeque<EventTask>,
    block: suspend () -> T
) : AbstractCoroutine<T>(context, block) {

    fun joinBlocking() {
        while (!isCompleted) {
            eventQueue.take().invoke()
        }
    }
}