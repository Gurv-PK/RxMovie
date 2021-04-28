package com.example.rxmovie.ui.singlemovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.rxmovie.R
import com.example.rxmovie.data.api.MovieDBInterface
import com.example.rxmovie.data.api.POSTER_BASE_URL
import com.example.rxmovie.data.api.TheMovieDBClient
import com.example.rxmovie.data.repository.NetworkState
import com.example.rxmovie.data.vo.MovieDetails
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var movieViewModel: SingleMovieViewModel
    private lateinit var movieDetailRepository: MovieDetailRepository

    private lateinit var moviename: TextView
    private lateinit var subtitle: TextView
    private lateinit var releaseDate: TextView
    private lateinit var rating: TextView
    private lateinit var runTime: TextView
    private lateinit var budget: TextView
    private lateinit var revenue: TextView
    private lateinit var overview: TextView
    private lateinit var image: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var cerror: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)


        moviename = findViewById(R.id.moviename)
        subtitle = findViewById(R.id.subtitle)
        releaseDate = findViewById(R.id.releasedate)
        rating = findViewById(R.id.rating)
        runTime = findViewById(R.id.runtime)
        budget = findViewById(R.id.budget)
        revenue = findViewById(R.id.revenue)
        overview = findViewById(R.id.overview)
        image = findViewById(R.id.ivposter)
        progressBar = findViewById(R.id.pbar)
        cerror = findViewById(R.id.cerr)

        val movieId: Int = intent.getIntExtra("id",1)
        val apiService : MovieDBInterface = TheMovieDBClient.getClient()
        movieDetailRepository = MovieDetailRepository(apiService)

        movieViewModel = getViewModel(movieId)

        movieViewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        movieViewModel.networkState.observe(this, Observer {
            progressBar.visibility = if(it == NetworkState.Loading) View.VISIBLE else View.GONE
            cerror.visibility = if(it == NetworkState.Error) View.VISIBLE else View.GONE
        })

    }

    private fun bindUI(it: MovieDetails) {

        moviename.text = it.title
        subtitle.text = it.tagline
        releaseDate.text = it.releaseDate
        rating.text = it.rating.toString()
        runTime.text = it.runtime.toString()
        overview.text = it.overview

        val formatCurrency  = NumberFormat.getCurrencyInstance(Locale.US)
        budget.text = formatCurrency.format(it.budget)
        revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(image)



    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {

        return ViewModelProviders.of(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(movieDetailRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]

    }
}