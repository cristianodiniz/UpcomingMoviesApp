package com.arctouch.codechallenge.detail


import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.widget.ImageView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.synnapps.carouselview.ImageListener
import com.arctouch.codechallenge.model.*
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var movieId = intent.getIntExtra("movieId",0).toLong()
       // progressBar.visibility = View.VISIBLE
        api.movie(movieId,TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    setContentOnView(it)
                  //  progressBar.visibility = View.GONE
                }


    }
    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    fun setContentOnView(data: Movie){

        var imgs = arrayOf(data.posterPath,data.backdropPath);

        carouselView.setImageListener( object : ImageListener {
            override fun setImageForPosition(position: Int, imageView: ImageView) {
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                Glide.with(this@MovieDetailActivity)
                        .load(imgs[position]?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(imageView)
            }
        })

        nameTextView.text = data.title
        releaseDateTextView.text = data.releaseDate
        overviewTextView.text = data.overview

        genresRecyclerView.layoutManager = GridLayoutManager(this@MovieDetailActivity, 5)
        genresRecyclerView.adapter = GenreAdapter(data.genres!!)

        carouselView.pageCount = imgs.size


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }




}
