package com.example.cinemashift.presentation.screen.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemashift.R
import com.example.cinemashift.databinding.FragmentMovieListBinding
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.presentation.adapter.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieListViewModel by viewModels()
    private val adapter = MovieListAdapter { movieId ->
        findNavController().navigate(
            MovieListFragmentDirections.toMovieDetail(movieId)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        binding.moviesRecyclerView.apply {
            adapter = this@MovieListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.item_margin)
                )
            )
        }
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MovieListUiState.Loading -> showLoading()
                is MovieListUiState.Success -> showContent(state.movies)
                is MovieListUiState.Error -> showError(state.message)
                MovieListUiState.Initial -> TODO()
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            moviesRecyclerView.isVisible = false
            errorLayout.root.isVisible = false
        }
    }

    private fun showContent(movies: List<Movie>) {
        binding.apply {
            progressBar.isVisible = false
            moviesRecyclerView.isVisible = true
            errorLayout.root.isVisible = false
            adapter.updateMovies(movies)
        }
    }

    private fun showError(message: String) {
        binding.apply {
            progressBar.isVisible = false
            moviesRecyclerView.isVisible = false
            errorLayout.root.isVisible = true
            errorLayout.errorMessageText.text = message
            errorLayout.retryButton.setOnClickListener {
                viewModel.loadMovies()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
