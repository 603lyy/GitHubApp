package com.kotlin.githubapp

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDex
import com.bennyhuo.tieguanyin.runtime.core.ActivityBuilder

private lateinit var INSTANCE: Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        ActivityBuilder.INSTANCE.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        MultiDex.install(base)
        super.attachBaseContext(base)
    }
}

object AppContext : ContextWrapper(INSTANCE)