package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.library.async
import com.bennyhuo.coroutines.library.delay
import com.bennyhuo.coroutines.library.launch
import com.bennyhuo.coroutines.library.runBlocking
import com.bennyhuo.coroutines.utils.log

fun main(args: Array<String>) = runBlocking {
    launch {
        log(-1)
        val result = async {
            log(1)
            delay(100)
            log(2)
            loadForResult().also {
                log(3)
            }
        }
        log(-2)
        delay(200)
        log(-3)
        log(result.await())
        log(-4)
    }.join()
}

suspend fun loadForResult(): String {
    delay(1000L)
    return "HelloWorld"
}