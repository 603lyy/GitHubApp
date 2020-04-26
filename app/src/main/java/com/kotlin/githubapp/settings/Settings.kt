package com.kotlin.githubapp.settings

import com.kotlin.common.sharepreferences.Preference
import com.kotlin.githubapp.AppContext
import com.kotlin.githubapp.R
import com.kotlin.githubapp.model.account.AccountManager
import com.kotlin.githubapp.utils.pref

object Settings {

    var lastPage: Int
        get() = if (lastPageIdString.isEmpty()) 0 else AppContext.resources.getIdentifier(
            lastPageIdString,
            "id",
            AppContext.packageName
        )
        set(value) {
            lastPageIdString = AppContext.resources.getResourceEntryName(value)
        }

    val defaultPage
        get() = if (AccountManager.isLoginIn()) defaultPageForUser else defaultPageForVisitor

    private var defaultPageForUser by pref(R.id.navRepos)

    private var defaultPageForVisitor by pref(R.id.navRepos)

    private var lastPageIdString by pref("")
}