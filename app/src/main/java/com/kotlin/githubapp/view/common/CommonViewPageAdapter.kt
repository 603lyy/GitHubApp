package com.kotlin.githubapp.view.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.kotlin.githubapp.utils.ViewPagerAdapterList
import com.kotlin.githubapp.view.config.FragmentPage


class CommonViewPageAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {

    val fragmentPages = ViewPagerAdapterList<FragmentPage>(this)

    override fun getItem(position: Int): Fragment {
        return fragmentPages[position].fragment
    }

    override fun getCount(): Int {
        return fragmentPages.size
    }

    override fun getItemPosition(fragmnet: Any): Int {
        for ((index, page) in fragmentPages.withIndex()) {
            if (fragmnet == page.fragment) {
                return index
            }
        }

        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentPages[position].title
    }

}