package com.kotlin.githubapp.presenter

import android.util.Log
import com.kotlin.githubapp.model.account.AccountManager
import com.kotlin.githubapp.view.LoginActivity
import com.kotlin.mvp.impl.BasePresenter


class LoginPresenter : BasePresenter<LoginActivity>() {

    fun doLogin(name: String, password: String) {
        AccountManager.username = name
        AccountManager.password = password
        view.onLoginStart()
        AccountManager.login2()
            .subscribe({
                view.onLoginSuccess()
            }, {
                view.onLoginError(it)
            })
    }

    fun checkUserName(name: String): Boolean {
        return true
    }

    fun checkPassword(password: String): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        Log.i("lyy", AccountManager.toString())
        view.onDataInit(AccountManager.username, AccountManager.password)
    }

}