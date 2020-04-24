package com.kotlin.githubapp.view.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter
import com.kotlin.githubapp.R
import com.kotlin.githubapp.model.page.ListPage
import com.kotlin.githubapp.view.widget.ErrorInfoView
import com.kotlin.mvp.impl.BaseFragment
import kotlinx.android.synthetic.main.list.*
import org.jetbrains.anko.sdk15.listeners.onClick
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.toast
import retrofit2.adapter.rxjava.GitHubPaging

abstract class CommonListFragment<DataType, out presenter : CommonListPresenter<DataType, CommonListFragment<DataType, presenter>>> :
    BaseFragment<presenter>() {

    protected abstract val adapter: CommonListAdapter<DataType>

    protected val errorInfoView by lazy {
        ErrorInfoView(rootView)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshView.setColorSchemeResources(
            R.color.google_red,
            R.color.google_yellow,
            R.color.google_green,
            R.color.google_blue
        )

        recyclerView.adapter = LuRecyclerViewAdapter(adapter)
        recyclerView.setLoadMoreEnabled(true)
        recyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()

        refreshView.isRefreshing = true

        recyclerView.setOnLoadMoreListener(presenter::loadMore)

        refreshView.onRefresh(presenter::refreshData)
        presenter.initData()
    }

    fun setLoadMoreEnable(isEnable: Boolean) {
        recyclerView.setLoadMoreEnabled(isEnable)
    }

    fun onDataInit(data: GitHubPaging<DataType>) {
        adapter.data.clear()
        adapter.data.addAll(data)
        recyclerView.setNoMore(data.isLast)
        recyclerView.refreshComplete(ListPage.PAGE_SIZE)
        refreshView.isRefreshing = false
        dismissError()
    }

    fun onDataRefresh(data: GitHubPaging<DataType>) {
        onDataInit(data)
    }

    fun onDataRefreshWithError(error: String){
        if (adapter.data.isEmpty()){
            showError(error)
            errorInfoView.onClick {
                presenter.initData()
            }
        } else {
            toast(error)
        }
    }

    fun onDataInitWithNothing() {
        showError("No Data")
        recyclerView.setNoMore(true)
        recyclerView.refreshComplete(ListPage.PAGE_SIZE)
        refreshView.isRefreshing = false
        errorInfoView.isClickable = false
    }

    fun onDataInitWithError(error: String){
        showError(error)
        errorInfoView.onClick {
            presenter.initData()
        }
    }

    fun onMoreDataLoaded(data: GitHubPaging<DataType>){
        adapter.data.update(data)
        recyclerView.refreshComplete(ListPage.PAGE_SIZE)
        recyclerView.setNoMore(data.isLast)
        dismissError()
    }

    fun onMoreDataLoadedWithError(error: String){
        showError(error)
        recyclerView.refreshComplete(ListPage.PAGE_SIZE)
        errorInfoView.onClick {
            presenter.initData()
        }
    }

    protected open fun showError(error: String) {
        errorInfoView.show(error)
    }

    protected open fun dismissError() {
        errorInfoView.dismiss()
    }
}