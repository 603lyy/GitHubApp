package com.kotlin.githubapp.utils

import cn.carbs.android.avatarimageview.library.AppCompatAvatarImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

/**
 * Created by benny on 7/4/17.
 */
fun AppCompatAvatarImageView.loadWithGlide(url: String, textPlaceHolder: Char, requestOptions: RequestOptions = RequestOptions()){
    textPlaceHolder.toString().let {
        setTextAndColorSeed(it.toUpperCase(Locale.ROOT), it.hashCode().toString())
    }

    Glide.with(this.context)
            .load(url)
            .apply(requestOptions)
            .into(this);
}