package com.kotlin.githubapp.utils

import com.kotlin.common.sharepreferences.Preference
import com.kotlin.githubapp.AppContext
import kotlin.reflect.jvm.jvmName

/**
 * Created by benny on 6/23/17.
 */
inline fun <reified R, T> R.pref(default: T) = Preference(AppContext, "", default, R::class.jvmName)