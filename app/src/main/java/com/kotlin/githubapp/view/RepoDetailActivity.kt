package com.kotlin.githubapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bennyhuo.tieguanyin.annotations.ActivityBuilder
import com.bennyhuo.tieguanyin.annotations.Required
import com.kotlin.githubapp.R
import com.kotlin.githubapp.network.entities.Repository
import com.kotlin.githubapp.network.services.ActivityService
import com.kotlin.githubapp.network.services.RepositoryService
import com.kotlin.githubapp.utils.*
import com.kotlin.githubapp.view.config.Themer
import kotlinx.android.synthetic.main.activity_repo_detail.*
import kotlinx.android.synthetic.main.app_bar_details.*
import retrofit2.Response
import rx.Subscriber

@ActivityBuilder
class RepoDetailActivity : AppCompatActivity() {

    //    @Required
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Themer.applyProperTheme(this)
        setContentView(R.layout.activity_repo_detail)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent.getParcelableExtra<Repository>("repository").let {
            it ?: return
            repository = it
        }

        initDetails()
        reloadDetails()
    }

    private fun initDetails() {
        avatarView.loadWithGlide(repository.owner.avatar_url, repository.owner.login.first())
        collapsingToolbar.title = repository.name

        descriptionView.markdownText = getString(
            R.string.repo_description_template,
            repository.owner.login,
            repository.owner.html_url,
            repository.name,
            repository.html_url,
            repository.owner.login,
            repository.owner.html_url,
            githubTimeToDate(repository.created_at)?.view()
        )

        bodyView.text = repository.description

        detailContainer.alpha = 0f

        stars.checkEvent = { isChecked ->
            if (isChecked) {
                ActivityService.unstar(repository.owner.login, repository.name)
                    .map { false }
            } else {
                ActivityService.star(repository.owner.login, repository.name)
                    .map { true }
            }.doOnNext { reloadDetails(true) }
        }

        watches.checkEvent = { isChecked ->
            if (isChecked) {
                ActivityService.unwatch(repository.owner.login, repository.name)
                    .map { false }
            } else {
                ActivityService.watch(repository.owner.login, repository.name)
                    .map { true }
            }.doOnNext { reloadDetails(true) }
        }

        ActivityService.isStarred(repository.owner.login, repository.name)
            .onErrorReturn {
                if (it is retrofit2.HttpException) {
                    it.response() as Response<Any>
                } else {
                    throw it
                }
            }
            .subscribeIgnoreError {
                stars.isChecked = it.isSuccessful
            }

        ActivityService.isWatched(repository.owner.login, repository.name)
            .subscribeIgnoreError {
                watches.isChecked = it.subscribed
            }
    }

    private fun reloadDetails(forceNetwork: Boolean = false) {
        RepositoryService.getRepository(repository.owner.login, repository.name, forceNetwork)
            .subscribe(object : Subscriber<Repository>() {
                override fun onStart() {
                    super.onStart()
                    loadingView.animate().alpha(1f).start()
                }

                override fun onNext(t: Repository) {
                    repository = t

                    owner.content = repository.owner.login
                    stars.content = repository.stargazers_count.toString()
                    watches.content = repository.subscribers_count.toString()
                    forks.content = repository.forks_count.toString()
                    issues.content = repository.open_issues_count.toString()

                    loadingView.animate().alpha(0f).start()
                    detailContainer.animate().alpha(1f).start()
                }

                override fun onCompleted() = Unit

                override fun onError(e: Throwable) {
                    loadingView.animate().alpha(0f).start()
                    e.printStackTrace()
                }

            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
