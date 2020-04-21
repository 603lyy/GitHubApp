package com.kotlin.common.sharepreferences

import android.content.Context
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(
    private val context: Context,
    val keyName: String,
    val defaultValue: T,
    val preName: String = "default"
) : ReadWriteProperty<Any?, T> {

    private val prefs by lazy {
        context.getSharedPreferences(preName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(findProperName(property))
    }

    private fun findProperName(property: KProperty<*>) =
        if (keyName.isEmpty()) property.name else keyName

    private fun findPreference(key: String): T {
        return when (defaultValue) {
            is Long -> prefs.getLong(key, defaultValue)
            is Int -> prefs.getInt(key, defaultValue)
            is Boolean -> prefs.getBoolean(key, defaultValue)
            is String -> prefs.getString(key, defaultValue)
            else -> throw IllegalArgumentException("unsupported type.")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(findProperName(property), value)
    }

    private fun putPreference(key: String, value: T) {
        with(prefs.edit()) {
            when (value) {
                is Long -> putLong(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("unsupported type.")
            }
        }.apply()
    }

}
