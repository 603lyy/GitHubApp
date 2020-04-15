package com.kotlin.githubapp.network.services

import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.network.retrofit
import retrofit2.http.GET
import rx.Observable

interface UserApi {

    @GET("/user")
    fun getAuthenticatedUser(): Observable<User>
}

object UserService : UserApi by retrofit.create(UserApi::class.java)