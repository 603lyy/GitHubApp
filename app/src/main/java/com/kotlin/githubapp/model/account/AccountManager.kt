package com.kotlin.githubapp.model.account

import com.google.gson.Gson
import com.kotlin.githubapp.network.entities.AuthorizationReq
import com.kotlin.githubapp.network.entities.AuthorizationRsp
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.network.services.AuthService
import com.kotlin.githubapp.network.services.UserService
import com.kotlin.githubapp.utils.fromJson
import com.kotlin.githubapp.utils.pref
import retrofit2.HttpException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.lang.Exception

interface OnAccountStatusChangeListener {
    fun onLogin(user: User)

    fun onLogout()
}

object AccountManager {

    var username by pref("")
    var password by pref("")
    var authId by pref(-1)
    var token by pref("")

    private var userJson by pref("")

    private var currentUser: User? = null
        get() {
            if (field == null && userJson.isNotEmpty()) {
                field = Gson().fromJson<User>(userJson)
            }
            return field
        }
        set(value) {
            if (value == null) {
                userJson = ""
            } else {
                userJson = Gson().toJson(value)
            }

            field = value
        }

    private val onAccountStatusChangeListenerList = ArrayList<OnAccountStatusChangeListener>()

    private fun notifyLogin(user: User) {
        onAccountStatusChangeListenerList.forEach {
            it.onLogin(user)
        }
    }

    private fun notifyLogout() {
        onAccountStatusChangeListenerList.forEach {
            it.onLogout()
        }
    }

    fun isLoginIn(): Boolean = token.isNotEmpty()

    fun login() =
        AuthService.createAuthorization(AuthorizationReq())
            .doOnNext {
                if (token.isEmpty()) throw AccountException(it)
            }
            .retryWhen {
                it.flatMap {
                    if (it is AccountException) {
                        AuthService.deleteAuthorization(it.authorizationRsp.id)
                    } else {
                        Observable.error(it)
                    }
                }
            }
            .flatMap {
                token = it.token
                authId = it.id
                UserService.getAuthenticatedUser()
            }
            .map {
                currentUser = it
                notifyLogin(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    fun logout() = AuthService.deleteAuthorization(authId)
        .doOnNext {
            if (it.isSuccessful) {
                currentUser = null
                authId = -1;
                token = ""
                notifyLogout()
            } else {
                throw HttpException(it)
            }

        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

    class AccountException(val authorizationRsp: AuthorizationRsp) : Exception("Already logged in.")
}