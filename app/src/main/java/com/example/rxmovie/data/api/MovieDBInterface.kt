package com.example.rxmovie.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.rxmovie.data.vo.MovieDetails
import com.example.rxmovie.data.vo.MovieResponse
import retrofit2.http.Query

interface MovieDBInterface {

    @GET("movie/popular")
    fun getPopular(@Query("page") page: Int) : Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int): Single<MovieDetails>

}