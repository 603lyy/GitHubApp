package com.kotlin.githubapp.settings

import com.kotlin.common.sharepreferences.Preference
import com.kotlin.githubapp.AppContext

object Setting {
    var username:String by Preference(
        AppContext,
        "username",
        ""
    )
    var password:String by Preference(
        AppContext,
        "password",
        ""
    )
}