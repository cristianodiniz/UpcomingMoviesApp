package com.arctouch.codechallenge.detail

import android.content.Context
import com.arctouch.codechallenge.model.Movie

interface DetailActivityMVP {

    interface ModelImpl {

        fun getMovie(movieId:Int)


    }
    interface PresenterImpl {

        val context: Context
        fun getMovie(movieId:Int)
        fun setMovie(data:Movie)
        fun isLoading(value:Boolean)
        fun isLoading():Boolean

        fun setView(view: DetailActivityMVP.ViewImpl)

    }

    interface ViewImpl {
        val KEY: String
            get() = "DetailActivity"

        fun setContentOnView(data: Movie)

        fun showProgressBar(show: Boolean)


    }
}