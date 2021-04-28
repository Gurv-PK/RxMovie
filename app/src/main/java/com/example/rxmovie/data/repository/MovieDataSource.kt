package com.example.rxmovie.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.rxmovie.data.api.FIRST_PAGE
import com.example.rxmovie.data.api.MovieDBInterface
import com.example.rxmovie.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val apiService: MovieDBInterface, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()



    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {

        networkState.postValue(NetworkState.Loading)
        compositeDisposable.add(
                apiService.getPopular(page)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                {
                                    callback.onResult(it.movieList,null,page+1)
                                    networkState.postValue(NetworkState.Loaded)
                                },
                                {
                                    networkState.postValue(NetworkState.Error)
                                }
                        )
        )

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.Loading)
        compositeDisposable.add(
                apiService.getPopular(params.key)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                {
                                    if(it.totalPages >= params.key)
                                    {
                                        callback.onResult(it.movieList,params.key+1)
                                        networkState.postValue(NetworkState.Loaded)
                                    }
                                    else
                                    {
                                        networkState.postValue(NetworkState.Endoflist)
                                    }
                                },
                                {
                                    networkState.postValue(NetworkState.Error)
                                }
                        )
        )
    }
}