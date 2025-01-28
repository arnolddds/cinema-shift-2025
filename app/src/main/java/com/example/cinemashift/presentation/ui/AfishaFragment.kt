package com.example.cinemashift.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.cinemashift.R
import com.example.cinemashift.databinding.FragmentAfishaBinding

class AfishaFragment : Fragment(R.layout.fragment_afisha) {

    private var _binding: FragmentAfishaBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AfishaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAfishaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadMovies()
    }

    private fun setupRecyclerView() {
        val adapter = MoviesAdapter()
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            (binding.recyclerView.adapter as MoviesAdapter).submitList(movies)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
