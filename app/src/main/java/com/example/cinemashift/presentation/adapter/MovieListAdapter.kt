package com.example.cinemashift.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemashift.R
import com.example.cinemashift.databinding.ItemMovieBinding
import com.example.cinemashift.presentation.model.MovieUI

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {
    private val movies = mutableListOf<MovieUI>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun setMovies(newMovies: List<MovieUI>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.movieTitleTextView)
        private val ratingBar: RatingBar = view.findViewById(R.id.movieRatingBar)
        private val imageView: ImageView = view.findViewById(R.id.movieImageView)

        fun bind(movie: MovieUI) {
            titleTextView.text = movie.title
            ratingBar.rating = movie.rating
            // Загрузка изображения будет добавлена позже
        }
    }
}