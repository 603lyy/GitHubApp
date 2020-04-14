package com.kotlin.githubapp

import com.kotlin.common.Preferences

object Setting {
    var username:String by Preferences(AppContext,"username","")
    var password:String by Preferences(AppContext,"password","")
}