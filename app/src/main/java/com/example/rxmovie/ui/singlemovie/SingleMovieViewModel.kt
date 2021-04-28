package com.example.rxmovie.ui.singlemovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rxmovie.data.repository.NetworkState
import com.example.rxmovie.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieDetailRepository: MovieDetailRepository, movieId: Int) : ViewModel(){

    private val compositeDisposable =  CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {
        movieDetailRepository.fetchingSIngleMovieDetails(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieDetailRepository.getMovieDetailsResponse()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}