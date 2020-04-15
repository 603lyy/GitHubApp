package com.kotlin.common

import com.kotlin.common.ext.otherwise
import com.kotlin.common.ext.yes
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testBoolean(){
        val result =true.yes {
            1
        }.otherwise {
            2
        }

        assertEquals(result,2)
    }
}
