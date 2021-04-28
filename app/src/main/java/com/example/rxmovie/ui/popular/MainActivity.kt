package com.example.rxmovie.ui.popular

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rxmovie.R
import com.example.rxmovie.data.api.MovieDBInterface
import com.example.rxmovie.data.api.TheMovieDBClient
import com.example.rxmovie.data.repository.NetworkState

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainActivityViewModel

    lateinit var movieRepository: MoviePagedListRepository


    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.pbar)
        errorText = findViewById(R.id.cerr)
        recyclerView = findViewById(R.id.movie_list)




        val apiService : MovieDBInterface = TheMovieDBClient.getClient()

        movieRepository = MoviePagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviePageListAdaptor(this)

        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return  1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else return 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        };


        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = movieAdapter

        viewModel.moviePageList.observe(this, androidx.lifecycle.Observer{
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, androidx.lifecycle.Observer {
            progressBar.visibility = if (viewModel.listIsEmpty() && it == NetworkState.Loading) View.VISIBLE else View.GONE
            errorText.visibility = if (viewModel.listIsEmpty() && it == NetworkState.Error) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setnNetworkState(it)
            }
        })

    }


    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

}