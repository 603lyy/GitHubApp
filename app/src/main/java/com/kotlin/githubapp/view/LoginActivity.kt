package com.kotlin.githubapp.view

import android.os.Bundle

import com.kotlin.githubapp.R
import com.kotlin.githubapp.presenter.LoginPresenter
import com.kotlin.mvp.impl.BaseActivity

class LoginActivity : BaseActivity<LoginPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

    }

}
