package com.arctouch.codechallenge.home

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arctouch.codechallenge.adpters.HomeAdapter
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.PaginationScrollListener


class HomeActivityPresenter(override val context: Context) : HomeActivityMVP.PresenterImpl {

    private var model: HomeActivityMVP.ModelImpl = HomeActivityModel(this)
    private lateinit var view: HomeActivityMVP.ViewImpl

    override var isLastPage = false
    override var numberOfPages = 0
    override var currentPage = 0

    private var loading = false
    override fun isLoading(value: Boolean) {
        this@HomeActivityPresenter.loading = value
        view.showProgressBar(value)
    }

    override fun isLoading(): Boolean {
        return this@HomeActivityPresenter.loading
    }

    override var list = ArrayList<Movie>()
    override lateinit var adapter: HomeAdapter

    override fun setView(view: HomeActivityMVP.ViewImpl) {
        this.view = view
    }

    override fun startUpcomingMovies() {
        model.startUpcomingMovies()
    }

    override fun setupRecyclerView(recyclerView: RecyclerView) {

        val linearLayoutManager = LinearLayoutManager(context)
        this@HomeActivityPresenter.adapter = HomeAdapter(this@HomeActivityPresenter.list)
        recyclerView.adapter = this@HomeActivityPresenter.adapter
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun getTotalPageCount(): Int {
                return this@HomeActivityPresenter.numberOfPages
            }

            override fun loadMoreItems() {
                this@HomeActivityPresenter.isLoading(true)
                this@HomeActivityPresenter.currentPage += 1
                if (this@HomeActivityPresenter.currentPage < this@HomeActivityPresenter.numberOfPages) {
                    this@HomeActivityPresenter.model.getUpcomingMovies(this@HomeActivityPresenter.currentPage)
                }
            }

            override fun isLastPage(): Boolean {
                return this@HomeActivityPresenter.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@HomeActivityPresenter.isLoading()
            }
        })
    }




}