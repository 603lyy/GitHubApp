package com.kotlin.githubapp.utils

import android.widget.TextView
import com.zzhoujay.richtext.RichText

/**
 * Created by benny on 10/15/17.
 */
var TextView.markdownText: String
    set(value) {
        RichText.fromMarkdown(value).into(this)
    }
    get() = text.toString()