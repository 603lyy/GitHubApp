package com.kotlin.githubapp.network.services

import com.kotlin.githubapp.network.entities.AuthorizationReq
import com.kotlin.githubapp.network.entities.AuthorizationRsp
import com.kotlin.githubapp.network.retrofit
import com.kotlin.githubapp.settings.Configs
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface AuthApi {

    @PUT("/authorization/clients/${Configs.Account.clientId}/{fingerPrint}")
    fun createAuthorization(
        @Body req: AuthorizationReq,
        @Path("fingerPrint") fingerPrint: String = Configs.Account.fingerPrint
    ): Observable<AuthorizationRsp>

    @DELETE("/authorization/{id}")
    fun deleteAuthorization(@Path("id") id: Int): Observable<Response<Any>>

    @GET("/user?access_token=${Configs.Account.accessToken}")
    fun getUser(): Observable<AuthorizationRsp>
}

object AuthService : AuthApi by retrofit.create(AuthApi::class.java)