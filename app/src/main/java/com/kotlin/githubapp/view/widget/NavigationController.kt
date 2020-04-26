package com.kotlin.githubapp.view.widget

import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import com.kotlin.common.log.logger
import com.kotlin.githubapp.R
import com.kotlin.githubapp.model.account.AccountManager
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.settings.Settings
import com.kotlin.githubapp.utils.doOnLayoutAvailable
import com.kotlin.githubapp.utils.loadWithGlide
import com.kotlin.githubapp.utils.selectItem
import com.kotlin.githubapp.view.config.NavViewItem
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk15.listeners.onClick

class NavigationController(
    private val navigationView: NavigationView,
    private val onNavItemChanged: (NavViewItem) -> Unit,
    private val onHeaderClick: () -> Unit
) : NavigationView.OnNavigationItemSelectedListener {

    init {
        navigationView.setNavigationItemSelectedListener(this)
    }

    private var currentItem: NavViewItem? = null

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationView.apply {
            Settings.lastPage = item.itemId
            val navItem = NavViewItem[item.itemId]
            onNavItemChanged(navItem)
        }
        return true
    }

    fun useLoginLayout() {
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.activity_main_drawer) //inflate new items.
        onUpdate(AccountManager.currentUser)
        selectProperItem()
    }

    fun useNoLoginLayout() {
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.activity_main_drawer_no_logged_in) //inflate new items.
        onUpdate(AccountManager.currentUser)
        selectProperItem()
    }

    private fun onUpdate(user: User?) {
        navigationView.doOnLayoutAvailable {
            navigationView.apply {
                usernameView.text = user?.login ?: "请登录"
                emailView.text = user?.email ?: "bennyhuo@kotliner.cn"
                if (user == null) {
                    avatarView.imageResource = R.drawable.ic_github
                } else {
                    avatarView.loadWithGlide(user.avatar_url, user.login.first())
                }

                navigationHeader.onClick { onHeaderClick() }
            }
        }
    }

    fun selectProperItem() {
        logger.debug("selectProperItem")
        navigationView.doOnLayoutAvailable {
            logger.debug("selectProperItem onLayout: $currentItem")
            ((currentItem?.let { NavViewItem[it] } ?: Settings.lastPage)
                    .takeIf { navigationView.menu.findItem(it) != null } ?: run { Settings.defaultPage })
                    .let(navigationView::selectItem)
        }
    }
}