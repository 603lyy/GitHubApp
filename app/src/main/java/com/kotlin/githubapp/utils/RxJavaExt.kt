package com.kotlin.githubapp.utils

import rx.Observable
import rx.Subscription

fun <T> Observable<T>.subscribeIgnoreError(onNext: (T) -> Unit): Subscription
    = subscribe(onNext, Throwable::printStackTrace)