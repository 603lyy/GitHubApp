package com.kotlin.githubapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kotlin.githubapp.model.account.AccountManager
import com.kotlin.githubapp.view.common.CommonViewPagerFragment
import com.kotlin.githubapp.view.config.FragmentPage
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textView

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