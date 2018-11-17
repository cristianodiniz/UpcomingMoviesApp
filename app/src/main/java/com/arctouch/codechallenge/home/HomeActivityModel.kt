package com.arctouch.codechallenge.home

import android.annotation.SuppressLint
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class HomeActivityModel(private val presenter: HomeActivityMVP.PresenterImpl) : HomeActivityMVP.ModelImpl {

    private val api: TmdbApi = Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi::class.java)

    @SuppressLint("CheckResult")
    override fun startUpcomingMovies(){
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    getUpcomingMovies(1)
                }
    }

    @SuppressLint("CheckResult")
    override fun getUpcomingMovies(page:Int) {
        presenter.isLoading(true)
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page,"")   /*TmdbApi.DEFAULT_REGION)*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { it ->
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    presenter.isLoading(false)
                    presenter.list.addAll(moviesWithGenres)
                    presenter.numberOfPages = it.totalPages
                    presenter.adapter.notifyDataSetChanged()
                }
    }
}