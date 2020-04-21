package com.kotlin.githubapp.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.ScrollView
import com.kotlin.common.ext.otherwise
import com.kotlin.common.ext.yes

import com.kotlin.githubapp.R
import com.kotlin.githubapp.presenter.LoginPresenter
import com.kotlin.githubapp.utils.hideSoftInput
import com.kotlin.mvp.impl.BaseActivity
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity<LoginPresenter>() {

    private val userName by lazy { findViewById<EditText>(R.id.username) }
    private val password by lazy { findViewById<EditText>(R.id.password) }
    private val signBtn by lazy { findViewById<Button>(R.id.signInButton) }
    private val loginForm by lazy { findViewById<View>(R.id.loginForm) }
    private val loginProgress by lazy { findViewById<ProgressBar>(R.id.loginProgress) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        signBtn.setOnClickListener {
            presenter.checkUserName(userName.text.toString())
                .yes {
                    presenter.checkPassword(password.text.toString())
                        .yes {
                            hideSoftInput()
                            presenter.doLogin(userName.text.toString(), password.text.toString())
                        }
                        .otherwise {
                            showTips(password, "密码不合法")
                        }
                }
                .otherwise {
                    showTips(password, "用户名不合法")
                }
        }
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        loginForm.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginForm.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        loginProgress.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginProgress.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    private fun showTips(view: EditText, tip: String) {
        view.requestFocus()
        view.error = tip
    }

    fun onLoginStart() {
        showProgress(true)
    }

    fun onLoginSuccess() {
        showProgress(false)
        toast("登录成功")
    }

    fun onLoginError(e: Throwable) {
        e.printStackTrace()
        showProgress(false)
        toast("登录失败")
    }

    fun onDataInit(name: String, password: String) {
        this.password.setText(password)
        this.userName.setText(name)
    }

}