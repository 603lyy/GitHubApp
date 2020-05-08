package com.kotlin.githubapp.view.fragment

import android.os.Bundle
import com.kotlin.githubapp.model.account.AccountManager
import com.kotlin.githubapp.view.common.CommonViewPagerFragment
import com.kotlin.githubapp.view.config.FragmentPage

class RepoFragment : CommonViewPagerFragment() {

    override fun getFragmentPagesLogin(): List<FragmentPage> {
        return listOf(
            FragmentPage(RepoListFragment().apply {
                arguments = Bundle().apply { putParcelable("userInfo", AccountManager.currentUser) }
            }, "My"),
            FragmentPage(RepoListFragment(), "All")
        )
    }

    override fun getFragmentPagesNotLogin(): List<FragmentPage> {
        return listOf(
            FragmentPage(RepoListFragment(), "All")
        )
    }
}