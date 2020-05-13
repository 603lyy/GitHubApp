package com.kotlin.githubapp.network

import com.apollographql.apollo.ApolloClient
import com.bennyhuo.retroapollo.RetroApollo
import com.bennyhuo.retroapollo.annotations.GraphQLQuery
import com.bennyhuo.retroapollo.rxjava.RxJavaCallAdapterFactory
import com.kotlin.githubapp.network.graphql.entities.RepositoryIssueCountQuery
import com.kotlin.githubapp.network.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

interface GraphQLApi {
    fun repositoryIssueCount(@GraphQLQuery("owner") owner: String, @GraphQLQuery("repo") repo: String)
            : Observable<RepositoryIssueCountQuery.Data>
}

private const val BASE_URL = "https://api.github.com/graphql"

val apolloClient by lazy {
    ApolloClient.builder()
        .serverUrl(BASE_URL)
        .okHttpClient(
            OkHttpClient.Builder().addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build())
        .build()
}

private val graphQLService by lazy {
    RetroApollo.Builder()
        .apolloClient(apolloClient)
        .addCallAdapterFactory(
            RxJavaCallAdapterFactory()
            .observableScheduler(AndroidSchedulers.mainThread())
            .subscribeScheduler(Schedulers.io()))
        .build()
        .createGraphQLService(GraphQLApi::class)
}

object GraphQLService: GraphQLApi by graphQLService