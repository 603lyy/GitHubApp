package com.kotlin.githubapp.model.repo

import com.kotlin.githubapp.model.page.ListPage
import com.kotlin.githubapp.network.entities.Repository
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.network.services.RepositoryService
import com.kotlin.githubapp.utils.format
import retrofit2.adapter.rxjava.GitHubPaging
import rx.Observable
import java.util.*

class RepoListPage(val owner: User?): ListPage<Repository>(){
    override fun getData(page: Int): Observable<GitHubPaging<Repository>> {
        return if(owner == null){
            RepositoryService.allRepositories(page, "pushed:<" + Date().format("yyyy-MM-dd"))
                .map {
                    it.paging
                }
        } else {
            RepositoryService.listRepositoriesOfUser(owner.login, page)
        }
    }

}