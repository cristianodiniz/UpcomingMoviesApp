package com.arctouch.codechallenge.home


import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.PaginationScrollListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : BaseActivity() {

    private var isLoading = false
    private var isLastPage = false
    private var numberOfPages = 0
    private var currentPage:Long = 0
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var list = ArrayList<Movie>()

    private lateinit var adapter:HomeAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)


        linearLayoutManager = LinearLayoutManager(this@HomeActivity)
        adapter = HomeAdapter(this@HomeActivity.list)
        recyclerView.adapter = adapter
        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun getTotalPageCount(): Int {
                return this@HomeActivity.numberOfPages
            }

            override fun loadMoreItems() {
                this@HomeActivity.isLoading = true
                currentPage += 1
                if (currentPage < numberOfPages) {
                    getUpcomingMovies(currentPage)
                }
            }

            override fun isLastPage(): Boolean {
                return this@HomeActivity.isLastPage
            }

            override fun isLoading(): Boolean {
                return this@HomeActivity.isLoading
            }
        })

        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    getUpcomingMovies(1)
                }


        // progressBar.visibility = View.GONE
    }

    fun getUpcomingMovies(page:Long) {
        this@HomeActivity.isLoading = true
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page,"")   //TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    this@HomeActivity.isLoading = false
                    this@HomeActivity.list.addAll(moviesWithGenres)
                    this@HomeActivity.numberOfPages = it.totalPages
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }
    }
}
