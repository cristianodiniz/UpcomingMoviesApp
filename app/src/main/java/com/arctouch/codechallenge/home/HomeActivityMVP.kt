package com.arctouch.codechallenge.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.arctouch.codechallenge.adpters.HomeAdapter
import com.arctouch.codechallenge.model.Movie

interface HomeActivityMVP {
    interface ModelImpl {

        fun startUpcomingMovies()
        fun getUpcomingMovies(page:Int)

    }
    interface PresenterImpl {

        val context: Context
        var isLastPage:Boolean
        fun isLoading(value:Boolean)
        fun isLoading():Boolean
        var numberOfPages:Int
        var currentPage:Int
        var list:ArrayList<Movie>
        var adapter: HomeAdapter

        fun setView(view: HomeActivityMVP.ViewImpl)

        fun startUpcomingMovies()

        fun setupRecyclerView(recyclerView: RecyclerView)


    }

    interface ViewImpl {
        //val KEY: String
        //    get() = "homeActivity"

        fun showProgressBar(show: Boolean)


    }
}