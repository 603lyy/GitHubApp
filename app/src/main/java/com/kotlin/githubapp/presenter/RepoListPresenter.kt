package com.kotlin.githubapp.presenter

import com.kotlin.githubapp.model.page.ListPage
import com.kotlin.githubapp.model.repo.RepoListPage
import com.kotlin.githubapp.network.entities.Repository
import com.kotlin.githubapp.view.common.CommonListPresenter
import com.kotlin.githubapp.view.fragment.RepoListFragment

class RepoListPresenter:CommonListPresenter<Repository,RepoListFragment>() {
    override val listPage by lazy {
        RepoListPage(view.user)
    }
}