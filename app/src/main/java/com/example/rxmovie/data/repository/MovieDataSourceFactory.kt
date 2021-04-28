package com.example.rxmovie.data.repository

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.rxmovie.data.api.MovieDBInterface
import com.example.rxmovie.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable


class MovieDataSourceFactory(private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}