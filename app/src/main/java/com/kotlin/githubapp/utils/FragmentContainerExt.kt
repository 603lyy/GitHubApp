package com.kotlin.githubapp.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Created by benny on 7/6/17.
 */
fun AppCompatActivity.showFragment(
    containerId: Int,
    clazz: Class<out Fragment>,
    vararg args: Pair<String, String>
) {
    supportFragmentManager.beginTransaction()
        .replace(
            containerId,
            clazz.newInstance().apply {
                arguments = Bundle().apply { args.forEach { putString(it.first, it.second) } }
            },
            clazz.name
        )
        .commitAllowingStateLoss()
}