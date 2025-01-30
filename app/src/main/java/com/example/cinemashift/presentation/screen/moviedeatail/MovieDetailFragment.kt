package com.example.cinemashift.presentation.screen.moviedeatail

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cinemashift.R
import com.example.cinemashift.databinding.FragmentMovieDetailBinding
import com.example.cinemashift.domain.entity.Schedule
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
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
        viewModel.loadMovie(args.movieId)
    }

    private fun getRatingColor(rating: Float): Int {
        return when {
            rating >= 7.0f -> ContextCompat.getColor(requireContext(), R.color.rating_high)
            rating >= 5.0f -> ContextCompat.getColor(requireContext(), R.color.rating_medium)
            else -> ContextCompat.getColor(requireContext(), R.color.rating_low)
        }
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
                kinopoiskRatingText.text = getString(R.string.kinopoisk_rating_format, movie.rating)

                ratingBar.progressTintList = ColorStateList.valueOf(getRatingColor(movie.rating))
                ratingBar.progressBackgroundTintList = ColorStateList.valueOf(getRatingColor(movie.rating))

                val baseUrl = getString(R.string.base_image_url)
                val imageUrl = if (movie.imageUrl.startsWith("http")) {
                    movie.imageUrl
                } else {
                    baseUrl + movie.imageUrl
                }

                Glide.with(requireContext())
                    .load(imageUrl)
                    .fitCenter()
                    .placeholder(R.drawable.placeholder_movie)
                    .error(R.drawable.error_movie)
                    .into(movieImageView)
            }
        }

        viewModel.schedule.observe(viewLifecycleOwner) { schedules ->
            setupDaysTabs(schedules)
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

    private fun setupDaysTabs(schedules: List<Schedule>) {
        binding.daysTabLayout.removeAllTabs()

        schedules.forEach { schedule ->
            binding.daysTabLayout.addTab(
                binding.daysTabLayout.newTab().setText(schedule.date)
            )
        }

        if (schedules.isNotEmpty()) {
            showSeancesForDay(schedules[0])
        }

        binding.daysTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                schedules.getOrNull(tab?.position ?: 0)?.let {
                    showSeancesForDay(it)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showSeancesForDay(schedule: Schedule) {
        binding.hallsContainer.removeAllViews()

        schedule.seances.groupBy { it.hall.name }.forEach { (hallName, seances) ->
            TextView(requireContext()).apply {
                text = hallName
                setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Subtitle1)
                setPadding(16, 16, 16, 8)
            }.also { binding.hallsContainer.addView(it) }

            FlexboxLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 0, 16, 16)
                }
                flexWrap = FlexWrap.WRAP
                justifyContent = JustifyContent.FLEX_START

                seances.forEach { seance ->
                    MaterialButton(
                        context,
                        null,
                        R.style.TimeButton
                    ).apply {
                        text = seance.time
                        layoutParams = FlexboxLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(4, 4, 4, 4)
                        }
                    }.also { addView(it) }
                }
            }.also { binding.hallsContainer.addView(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
