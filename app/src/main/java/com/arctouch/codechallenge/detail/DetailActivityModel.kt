package com.arctouch.codechallenge.detail

import android.annotation.SuppressLint
import com.arctouch.codechallenge.api.TmdbApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class DetailActivityModel(private val presenter: DetailActivityMVP.PresenterImpl): DetailActivityMVP.ModelImpl {

    private val api: TmdbApi = Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi::class.java)

    @SuppressLint("CheckResult")
    override fun getMovie(movieId: Int) {
        presenter.isLoading(true)
        api.movie(movieId, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    presenter.setMovie(it)
                    presenter.isLoading(false)
                }
    }

}