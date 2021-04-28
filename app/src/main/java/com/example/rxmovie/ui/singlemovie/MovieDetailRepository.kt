package com.example.rxmovie.ui.singlemovie

import androidx.lifecycle.LiveData
import com.example.rxmovie.data.api.MovieDBInterface
import com.example.rxmovie.data.repository.MovieDetailsNetworkDataSource
import com.example.rxmovie.data.repository.NetworkState
import com.example.rxmovie.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailRepository(private val apiservice: MovieDBInterface) {

    lateinit var movieDetailsNetworkSource: MovieDetailsNetworkDataSource

    fun fetchingSIngleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkSource = MovieDetailsNetworkDataSource(apiservice,compositeDisposable)
        movieDetailsNetworkSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkSource.movieDetailsResponse

    }

    fun getMovieDetailsResponse(): LiveData<NetworkState>{
        return movieDetailsNetworkSource.networkState
    }
}