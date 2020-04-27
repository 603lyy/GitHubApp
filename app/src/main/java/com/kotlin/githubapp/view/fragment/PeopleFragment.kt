package com.kotlin.githubapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kotlin.githubapp.view.common.CommonSinglePageFragment
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textView

/**
 * Created by benny on 7/16/17.
 */
class PeopleFragment : CommonSinglePageFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI { textView("Todo People") }.view
    }

}