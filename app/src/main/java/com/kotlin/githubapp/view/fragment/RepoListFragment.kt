package com.kotlin.githubapp.view.fragment

import android.content.Intent
import android.content.Intent.parseUri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bennyhuo.tieguanyin.annotations.FragmentBuilder
import com.kotlin.githubapp.network.entities.Repository
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.presenter.RepoListPresenter
import com.kotlin.githubapp.view.common.CommonListFragment

@FragmentBuilder
class RepoListFragment : CommonListFragment<Repository, RepoListPresenter>() {

    var user: User? = null

    override val adapter = RepoListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = arguments?.getParcelable("userInfo")
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}