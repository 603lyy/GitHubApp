package com.kotlin.githubapp.view.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kotlin.githubapp.view.MainActivity

abstract class CommonSinglePageFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).actionBarController.setupWithViewPager(null)
    }
}