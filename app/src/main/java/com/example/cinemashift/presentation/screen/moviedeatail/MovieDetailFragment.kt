package com.example.cinemashift.presentation.screen.moviedeatail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cinemashift.R
import com.example.cinemashift.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        Log.d("MovieDetailFragment", "Movie ID received: ${args.movieId}")
        viewModel.loadMovie(args.movieId)
    }

    private fun setupObservers() {
        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            with(binding) {
                titleTextView.text = movie.title
                genreYearTextView.text = getString(
                    R.string.genre_year_format,
                    movie.genres.firstOrNull() ?: "",
                    movie.country.name,
                    movie.releaseDate
                )
                descriptionTextView.text = movie.description
                typeTextView.text = getString(R.string.movie_type_film)
                ratingBar.rating = movie.rating / 2
                kinopoiskRatingText.text = getString(
                    R.string.kinopoisk_rating_format,
                    movie.rating
                )

                Glide.with(requireContext())
                    .load("https://shift-intensive.ru${movie.imageUrl}")
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_movie)
                    .error(R.drawable.error_movie)
                    .into(movieImageView)
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
