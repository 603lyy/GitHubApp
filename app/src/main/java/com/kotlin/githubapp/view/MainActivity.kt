package com.kotlin.githubapp.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bennyhuo.tieguanyin.annotations.ActivityBuilder
import com.kotlin.common.ext.no
import com.kotlin.common.ext.otherwise
import com.kotlin.common.ext.yes
import com.kotlin.githubapp.AppContext
import com.kotlin.githubapp.R
import com.kotlin.githubapp.model.account.AccountManager
import com.kotlin.githubapp.model.account.OnAccountStatusChangeListener
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.settings.Configs
import com.kotlin.githubapp.utils.*
import com.kotlin.githubapp.view.config.NavViewItem
import com.kotlin.githubapp.view.config.Themer
import com.kotlin.githubapp.view.fragment.AboutFragment
import com.kotlin.githubapp.view.widget.ActionBarController
import com.kotlin.githubapp.view.widget.NavigationController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.menu_item_daynight.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.sdk15.listeners.onCheckedChange
import org.jetbrains.anko.toast

@ActivityBuilder(flags = [Intent.FLAG_ACTIVITY_CLEAR_TOP])
class MainActivity : AppCompatActivity(), OnAccountStatusChangeListener {

    val actionBarController by lazy {
        ActionBarController(this)
    }

    private val navigationController by lazy {
        NavigationController(navigationView, ::onNavItemChanged, ::handleNavigationHeaderClickEvent)
    }

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Themer.applyProperTheme(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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

    }

    private fun initNavigationView() {
        AccountManager.isLoginIn()
            .yes {
                navigationController.useLoginLayout()
            }
            .otherwise {
                navigationController.useNoLoginLayout()
            }
        navigationController.selectProperItem()
    }

    private fun updateNavigationView(user: User) {
        navigationView.doOnLayoutAvailable {
            usernameView.text = user.login
            emailView.text = user.email ?: ""
            avatarView.loadWithGlide(user.avatar_url, user.login.first())
        }
    }

    override fun onLogin(user: User) {
        navigationController.useLoginLayout()
    }

    override fun onLogout() {
        navigationController.useNoLoginLayout()
    }

    private fun onNavItemChanged(navViewItem: NavViewItem) {
        drawer_layout.afterClosed {
            showFragment(R.id.fragmentContainer, navViewItem.fragmentClass, navViewItem.arguements)
            title = navViewItem.title
        }
    }

    private fun handleNavigationHeaderClickEvent() {
        AccountManager.isLoginIn().no {
            startActivity(Intent(AppContext, LoginActivity::class.java))
        }.otherwise {
            AccountManager.logoutDebug()
            toast("注销成功")
//            AccountManager
//                .logout()
//                .subscribe({
//                    toast("注销成功")
//                }, {
//                    it.printStackTrace()
//                })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_actionbar, menu)
        menu.findItem(R.id.dayNight).actionView.dayNightSwitch.apply {
            isChecked = Themer.currentTheme() == Themer.ThemeMode.DAY

            onCheckedChange { buttonView, isChecked ->
                Themer.toggle(this@MainActivity)
            }
        }
        return true
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
