package com.example.cinemashift.presentation.screen.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemashift.R
import com.example.cinemashift.databinding.FragmentMovieListBinding
import com.example.cinemashift.presentation.adapter.MovieListAdapter
import com.example.cinemashift.presentation.model.MovieUI

class MovieListFragment : Fragment() {
    private lateinit var adapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)

        // Добавим тестовые данные для проверки отображения
        val testMovies = listOf(
            MovieUI(
                id = 1,
                title = "Уикенд с батей",
                rating = 4.5f,
                imageUrl = ""
            ),
            MovieUI(
                id = 2,
                title = "Тестовый фильм 2",
                rating = 3.8f,
                imageUrl = ""
            ),
            MovieUI(
                id = 3,
                title = "Тестовый фильм 3",
                rating = 3.8f,
                imageUrl = ""
            ),
            MovieUI(
                id = 4,
                title = "Тестовый фильм 4",
                rating = 3.8f,
                imageUrl = ""
            ),
            MovieUI(
                id = 4,
                title = "Тестовый фильм 4",
                rating = 3.8f,
                imageUrl = ""
            ),
            MovieUI(
                id = 5,
                title = "Тестовый фильм 5",
                rating = 3.8f,
                imageUrl = ""
            )
        )
        adapter.setMovies(testMovies)
    }

    private fun setupRecyclerView(view: View) {
        adapter = MovieListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.moviesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
}