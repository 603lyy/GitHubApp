package com.kotlin.githubapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bennyhuo.tieguanyin.annotations.ActivityBuilder
import com.kotlin.common.ext.no
import com.kotlin.common.ext.otherwise
import com.kotlin.common.log.logger
import com.kotlin.githubapp.R
import com.kotlin.githubapp.model.account.AccountManager
import com.kotlin.githubapp.model.account.OnAccountStatusChangeListener
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.network.services.RepositoryService
import com.kotlin.githubapp.utils.doOnLayoutAvailable
import com.kotlin.githubapp.utils.format
import com.kotlin.githubapp.utils.loadWithGlide
import com.kotlin.githubapp.utils.showFragment
import com.kotlin.githubapp.view.fragment.AboutFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk15.listeners.onClick
import org.jetbrains.anko.toast
import java.util.*

@ActivityBuilder(flags = [Intent.FLAG_ACTIVITY_CLEAR_TOP])
class MainActivity : AppCompatActivity(), OnAccountStatusChangeListener {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        title = "About"

        toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        initNavigationView()

        AccountManager.onAccountStatusChangeListenerList.add(this)

        showFragment(R.id.fragmentContainer, AboutFragment::class.java)

        RepositoryService.allRepositories(2, "pushed:<" + Date().format("yyyy-MM-dd"))
            .subscribe({
                logger.debug("Paging: hasNext=${it.paging.hasNext}, hasPrev=${it.paging.hasPrev}")
            }, {
                it.printStackTrace()
            })
    }

    private fun initNavigationView() {
        AccountManager.currentUser?.let(::updateNavigationView) ?: clearNavigationView()
        initNavigationHeaderEvent()
    }

    private fun initNavigationHeaderEvent() {
        navigationView.doOnLayoutAvailable {
            navigationHeader.setOnClickListener {
                AccountManager.isLoginIn().no {

                }.otherwise {
                    AccountManager.logout().subscribe({
                        toast("注销成功")
                    }, {
                        it.printStackTrace()
                    })
                }
            }
        }
    }

    private fun updateNavigationView(user: User) {
        navigationView.doOnLayoutAvailable {
            usernameView.text = user.login
            emailView.text = user.email ?: ""
            avatarView.loadWithGlide(user.avatar_url, user.login.first())
        }
    }

    private fun clearNavigationView() {
        navigationView.doOnLayoutAvailable {
            usernameView.text = "请登录"
            emailView.text = ""
            avatarView.imageResource = R.drawable.ic_github
        }
    }

    override fun onLogin(user: User) {
        updateNavigationView(user)
    }

    override fun onLogout() {
        clearNavigationView()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        drawer_layout.removeDrawerListener(toggle)
        AccountManager.onAccountStatusChangeListenerList.remove(this)
    }
}
