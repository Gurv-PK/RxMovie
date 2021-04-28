package com.example.rxmovie.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rxmovie.data.api.MovieDBInterface
import com.example.rxmovie.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class MovieDetailsNetworkDataSource(private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _movieDetailsResponse = MutableLiveData<MovieDetails>()
    val movieDetailsResponse: LiveData<MovieDetails>
        get() = _movieDetailsResponse

    fun fetchMovieDetails(movieId: Int)
    {
        _networkState.postValue(NetworkState.Loading)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _movieDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.Loaded)
                    },
                        {
                            _networkState.postValue(NetworkState.Error)
                            System.out.println("Error is:"+it.message)
                        }
                    )

            )
        } catch (e: Exception){

        }
    }
}