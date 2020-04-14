package com.kotlin.common

import android.content.Context
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preferences<T>(
    val context: Context,
    val keyName: String,
    val defaultValue: T,
    val preName: String = "default"
) : ReadWriteProperty<Any?, T> {

    private val prefs by lazy {
        context.getSharedPreferences(preName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(keyName)
    }

    private fun findPreference(key: String): T {
        return when (defaultValue) {
            is Long -> prefs.getLong(key, defaultValue)
            is String -> prefs.getString(key, defaultValue)
            else -> throw IllegalArgumentException("unsupported type.")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(keyName,value)
    }

    private fun putPreference(key: String, value: T) {
        with(prefs.edit()) {
            when (value) {
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("unsupported type.")
            }
        }.apply()
    }

}
