package com.kotlin.githubapp.view.fragment
import com.bennyhuo.tieguanyin.annotations.FragmentBuilder
import com.bennyhuo.tieguanyin.annotations.Optional
import com.kotlin.githubapp.network.entities.Repository
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.presenter.RepoListPresenter
import com.kotlin.githubapp.view.common.CommonListFragment

@FragmentBuilder
class RepoListFragment : CommonListFragment<Repository, RepoListPresenter>() {

    var user: User? = null

    override val adapter = RepoListAdapter()
}