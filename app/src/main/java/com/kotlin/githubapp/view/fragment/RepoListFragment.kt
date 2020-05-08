package com.kotlin.githubapp.view.fragment

import android.os.Bundle
import android.view.View
import com.kotlin.githubapp.network.entities.Repository
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.presenter.RepoListPresenter
import com.kotlin.githubapp.view.common.CommonListFragment

class RepoListFragment : CommonListFragment<Repository, RepoListPresenter>() {

    var user: User? = null

    override val adapter = RepoListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        user = arguments?.getParcelable<User>("userInfo")
        super.onViewCreated(view, savedInstanceState)
    }
}