package com.kotlin.githubapp.view.config

import androidx.fragment.app.Fragment

interface ViewPagerFragmentConfig {
    fun getFragmentPagesLogin(): List<FragmentPage>
    fun getFragmentPagesNotLogin(): List<FragmentPage>
}

data class FragmentPage(val fragment: Fragment, val title: String)
