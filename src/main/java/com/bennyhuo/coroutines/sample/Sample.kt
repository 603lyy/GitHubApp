package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.library.launch
import com.bennyhuo.coroutines.utils.log

fun main(args: Array<String>) {
    log(1)
    launch {
        log(-1)
        log("HelloWorld")
        log(-2)
    }
    log(3)
    Thread.sleep(1000)
}

//fun main(args: Array<String>) = runBlocking {
//    launch {
//        log(-1)
//        val result = async {
//            log(1)
//            delay(100)
//            log(2)
//            loadForResult().also {
//                log(3)
//            }
//        }
//        log(-2)
//        delay(200)
//        log(-3)
//        log(result.await())
//        log(-4)
//    }.join()
//}

//suspend fun loadForResult(): String {
//    delay(1000L)
//    return "HelloWorld"
//}