package com.kotlin.githubapp.view.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.githubapp.R
import com.kotlin.githubapp.network.entities.Repository
import com.kotlin.githubapp.utils.loadWithGlide
import com.kotlin.githubapp.utils.toKilo
import com.kotlin.githubapp.view.common.CommonListAdapter
import kotlinx.android.synthetic.main.item_repo.view.*

class RepoListAdapter : CommonListAdapter<Repository>(R.layout.item_repo) {
    override fun onBindData(viewHolder: RecyclerView.ViewHolder, repository: Repository) {

        viewHolder.itemView.apply {
            avatarView.loadWithGlide(repository.owner.avatar_url, repository.owner.login.first())
            repoNameView.text = repository.name
            descriptionView.text = repository.description
            langView.text = repository.language ?: "Unknown"
            starView.text = repository.stargazers_count.toKilo()
            forkView.text = repository.forks_count.toKilo()
        }
    }

    override fun onItemClicked(itemView: View, item: Repository) {
    }
}