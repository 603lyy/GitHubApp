package com.kotlin.layout.v1

import android.app.Activity
import android.view.ViewGroup
import android.widget.LinearLayout

inline fun <reified T : ViewGroup> T.linearLayout(init: _LinearLayout.() -> Unit) {
    _LinearLayout(context).also(this::addView).also(init)
}

inline fun <reified T : ViewGroup> T.verticalLayout(init: _LinearLayout.() -> Unit) {
    _LinearLayout(context).also(this::addView)
        .apply {
            orientation = LinearLayout.VERTICAL
            init()
        }
}

inline fun <reified T : Activity> T.linearLayout(init: _LinearLayout.() -> Unit) {
    _LinearLayout(this).also(this::setContentView).also(init)
}

inline fun <reified T : Activity> T.verticalLayout(init: _LinearLayout.() -> Unit) {
    _LinearLayout(this).also(this::setContentView)
        .apply {
            orientation = LinearLayout.VERTICAL
            init()
        }
}