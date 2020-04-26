package com.kotlin.githubapp

import android.app.Application
import android.content.ContextWrapper
import com.bennyhuo.tieguanyin.runtime.core.ActivityBuilder

private lateinit var INSTANCE: Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        ActivityBuilder.INSTANCE.init(this)
    }
}

object AppContext : ContextWrapper(INSTANCE)