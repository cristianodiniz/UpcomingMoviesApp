package com.arctouch.codechallenge.detail

import android.content.Context
import com.arctouch.codechallenge.home.HomeActivityMVP
import com.arctouch.codechallenge.model.Movie

class DetailActivityPresenter(override val context: Context): DetailActivityMVP.PresenterImpl {

    private var model: DetailActivityMVP.ModelImpl = DetailActivityModel(this)
    private lateinit var view: DetailActivityMVP.ViewImpl

    override fun setView(view: DetailActivityMVP.ViewImpl) {
        this.view = view
    }

    override fun getMovie(movieId: Int) {
        model.getMovie(movieId)
    }

    override fun setMovie(data: Movie) {
        view.setContentOnView(data)
    }

    var loading = false
    override fun isLoading(value: Boolean) {
        view.showProgressBar(value)
        loading = value
    }

    override fun isLoading(): Boolean {
        return loading
    }
}