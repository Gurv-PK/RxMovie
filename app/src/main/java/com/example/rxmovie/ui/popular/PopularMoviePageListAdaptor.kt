package com.example.rxmovie.ui.popular

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rxmovie.R
import com.example.rxmovie.data.api.POSTER_BASE_URL
import com.example.rxmovie.data.repository.NetworkState
import com.example.rxmovie.data.vo.Movie
import com.example.rxmovie.ui.singlemovie.SingleMovie

class PopularMoviePageListAdaptor(private val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallBack()) {


    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if(viewType == MOVIE_VIEW_TYPE)
        {
            view = layoutInflater.inflate(R.layout.movie_item,parent,false)
            return MovieItemViewHolder(view)
        } else{
            view = layoutInflater.inflate(R.layout.network_data_error,parent,false)
            return NetworkItemViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }
        else
        {
            (holder as NetworkItemViewHolder).bind(networkState)
        }

    }

    private fun hasExtraRows(): Boolean{
        return networkState!=null && networkState != NetworkState.Loaded
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRows()) 1 else 0
    }


    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRows() && position == itemCount -1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    class MovieDiffCallBack : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(movie: Movie?, context: Context){
            val title : TextView
            val releaseDate : TextView
            val poster: ImageView

            title = itemView.findViewById(R.id.cvmoviename)
            releaseDate = itemView.findViewById(R.id.cvreleasedate)
            poster = itemView.findViewById(R.id.cvposter)

            releaseDate.text =  movie?.releaseDate
            title.text =  movie?.title


            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                    .load(moviePosterURL)
                    .into(poster);

            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }

        }

    }


    class NetworkItemViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val pbar : ProgressBar = itemView.findViewById(R.id.nvpbar)
        val terr : TextView = itemView.findViewById(R.id.nverr)

        fun bind(networkState: NetworkState?){
            if(networkState!=null && networkState == NetworkState.Loading)
            {
                pbar.visibility = View.VISIBLE
            }
            else
            {
                pbar.visibility = View.GONE
            }

            if(networkState!=null && networkState == NetworkState.Error)
            {
                terr.visibility = View.VISIBLE
                terr.text = networkState.msg
            }
            else if(networkState!=null && networkState == NetworkState.Endoflist)
            {
                terr.visibility = View.VISIBLE
                terr.text = networkState.msg
            }
            else
            {
                terr.visibility = View.GONE
            }



        }
    }

    fun setnNetworkState(newNetworkState: NetworkState){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRows()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRows()

        if(hadExtraRow != hasExtraRows())
            if(hadExtraRow)
            {
                notifyItemRemoved(super.getItemCount())
            }
            else
            {
                notifyItemInserted(super.getItemCount())
            }else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
                            notifyItemChanged(itemCount - 1)       //add the network message at the end
            }

    }

}