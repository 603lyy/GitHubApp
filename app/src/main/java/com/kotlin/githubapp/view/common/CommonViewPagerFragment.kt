package com.kotlin.githubapp.view.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.kotlin.githubapp.R
import com.kotlin.githubapp.model.account.AccountManager
import com.kotlin.githubapp.model.account.OnAccountStatusChangeListener
import com.kotlin.githubapp.network.entities.User
import com.kotlin.githubapp.view.MainActivity
import com.kotlin.githubapp.view.config.ViewPagerFragmentConfig
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.viewPager
import org.jetbrains.anko.verticalLayout

abstract class CommonViewPagerFragment : Fragment(), ViewPagerFragmentConfig,
    OnAccountStatusChangeListener {

    private val viewPageAdapter by lazy {
        CommonViewPageAdapter(childFragmentManager)
    }

    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return UI {
            verticalLayout {
                viewPager = viewPager {
                    id = R.id.viewPager
                }
                viewPager.adapter = viewPageAdapter
            }
        }.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).actionBarController.setupWithViewPager(viewPager)
        if (AccountManager.isLoginIn()) {
            viewPageAdapter.fragmentPages.addAll(getFragmentPagesLogin())
        } else {
            viewPageAdapter.fragmentPages.addAll(getFragmentPagesNotLogin())
        }
    }

    override fun onLogin(user: User) {
        viewPageAdapter.fragmentPages.clear()
        viewPageAdapter.fragmentPages.addAll(getFragmentPagesLogin())
    }

    override fun onLogout() {
        viewPageAdapter.fragmentPages.clear()
        viewPageAdapter.fragmentPages.addAll(getFragmentPagesNotLogin())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AccountManager.onAccountStatusChangeListenerList.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AccountManager.onAccountStatusChangeListenerList.remove(this)
    }

}