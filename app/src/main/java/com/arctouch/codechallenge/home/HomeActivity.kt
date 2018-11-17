package com.arctouch.codechallenge.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.R.*
import kotlinx.android.synthetic.main.home_activity.*


class HomeActivity : AppCompatActivity(), HomeActivityMVP.ViewImpl {

    private var presenter: HomeActivityMVP.PresenterImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.home_activity)

        if (presenter == null) presenter = HomeActivityPresenter(this)
        presenter?.setView(this)
        presenter?.setupRecyclerView(recyclerView)

    }

    override fun onStart() {
        super.onStart()
        presenter?.startUpcomingMovies()
    }


    override fun showProgressBar(show: Boolean) {
       if (show){
           progressBar.visibility = View.VISIBLE
       }else{
           progressBar.visibility = View.GONE
       }

    }


}
