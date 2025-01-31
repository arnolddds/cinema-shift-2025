package com.example.cinemashift.presentation.screen.moviedeatail

import android.content.res.ColorStateList
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cinemashift.R
import com.example.cinemashift.databinding.FragmentMovieDetailBinding
import com.example.cinemashift.domain.entity.Movie
import com.example.cinemashift.domain.entity.Schedule
import com.example.cinemashift.domain.entity.Seance
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModels()
    private var isDescriptionExpanded = false

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
        setupClickListeners()
        observeState()
        loadData()
    }

    private fun setupClickListeners() {
        with(binding) {
            backButton.setOnClickListener {
                NavHostFragment.findNavController(this@MovieDetailFragment).popBackStack()
            }
            expandButton.setOnClickListener {
                toggleDescription()
            }
            errorLayout.retryButton.setOnClickListener {
                loadData()
            }
        }
    }

    private fun observeState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MovieDetailUiState.Loading -> showLoading()
                is MovieDetailUiState.Success -> showContent(state.movie, state.schedule)
                is MovieDetailUiState.Error -> showError(state.message)
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.contentContainer.isVisible = false
        binding.errorLayout.root.isVisible = false
    }

    private fun showContent(movie: Movie, schedule: List<Schedule>) {
        binding.progressBar.isVisible = false
        binding.contentContainer.isVisible = true
        binding.errorLayout.root.isVisible = false
        bindMovieData(movie)
        setupDaysTabs(schedule)
    }

    private fun showError(message: String) {
        binding.progressBar.isVisible = false
        binding.contentContainer.isVisible = false
        binding.errorLayout.root.isVisible = true
        binding.errorLayout.errorMessageText.text = message
    }

    private fun loadData() {
        viewModel.loadMovie(args.movieId)
    }

    private fun toggleDescription() {
        isDescriptionExpanded = !isDescriptionExpanded
        TransitionManager.beginDelayedTransition(binding.root as ViewGroup)
        binding.descriptionTextView.maxLines = if (isDescriptionExpanded) Integer.MAX_VALUE else 3
        binding.expandButton.text = getString(
            if (isDescriptionExpanded) R.string.button_collapse else R.string.button_expand
        )
    }

    private fun bindMovieData(movie: Movie) {
        with(binding) {
            titleTextView.text = movie.title
            genreYearTextView.text = getString(
                R.string.genre_year_format,
                movie.genres.firstOrNull() ?: "",
                movie.country.name,
                movie.releaseDate
            )
            descriptionTextView.text = movie.description
            expandButton.isVisible = movie.description.length > 100
            ratingBar.rating = movie.rating / 2
            kinopoiskRatingText.text = getString(R.string.kinopoisk_rating_format, movie.rating)
            ratingBar.progressTintList = ColorStateList.valueOf(getRatingColor(movie.rating))
            loadImage(movie.imageUrl)
        }
    }

    private fun loadImage(imageUrl: String) {
        val baseUrl = getString(R.string.base_image_url)
        val fullUrl = if (imageUrl.startsWith("http")) imageUrl else baseUrl + imageUrl

        Glide.with(requireContext())
            .load(fullUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_movie)
            .error(R.drawable.error_movie)
            .into(binding.movieImageView)
    }

    private fun setupDaysTabs(schedules: List<Schedule>) {
        binding.daysTabLayout.removeAllTabs()
        schedules.forEach { schedule ->
            val tab = binding.daysTabLayout.newTab().apply {
                text = formatDate(schedule.date)
            }
            binding.daysTabLayout.addTab(tab)
        }


        binding.daysTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                schedules.getOrNull(tab?.position ?: 0)?.let { showSeancesForDay(it) }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        if (binding.daysTabLayout.tabCount > 0) {
            val firstTab = binding.daysTabLayout.getTabAt(0)
            firstTab?.select()
            schedules.firstOrNull()?.let { showSeancesForDay(it) }
        }
    }


    private fun formatDate(date: String): String {
        val parsedDate = SimpleDateFormat("dd.MM.yy", Locale.getDefault()).parse(date) ?: return ""
        val calendar = Calendar.getInstance().apply { time = parsedDate }
        val dayOfWeek = SimpleDateFormat("EEE", Locale("ru")).format(parsedDate)
        val monthName = SimpleDateFormat("LLLL", Locale("ru")).format(parsedDate)
        return "$dayOfWeek, ${calendar.get(Calendar.DAY_OF_MONTH)} $monthName"
    }


    private fun showSeancesForDay(schedule: Schedule) {
        binding.hallsContainer.removeAllViews()
        schedule.seances.groupBy { it.hall.name }.forEach { (hallName, seances) ->
            addHallTitle(hallName)
            addSeancesButtons(seances)
        }
    }

    private fun addHallTitle(hallName: String) {
        binding.hallsContainer.addView(TextView(requireContext()).apply {
            text = hallName
            setTextColor(ContextCompat.getColor(context, R.color.black))
            setTextSize(16f)
            setPadding(0, 16, 0, 8)
        })
    }

    private var selectedButton: MaterialButton? = null

    private fun addSeancesButtons(seances: List<Seance>) {
        val flexboxLayout = FlexboxLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

        seances.forEach { seance ->
            val button = MaterialButton(requireContext()).apply {
                text = seance.time
                setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setOnClickListener {
                    selectedButton?.let {
                        it.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                        it.isSelected = false
                    }
                    isSelected = true
                    setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
                    selectedButton = this
                }
            }
            flexboxLayout.addView(button)
        }

        binding.hallsContainer.addView(flexboxLayout)
    }


    private fun getRatingColor(rating: Float): Int {
        return when {
            rating >= 7.0f -> ContextCompat.getColor(requireContext(), R.color.rating_high)
            rating >= 5.0f -> ContextCompat.getColor(requireContext(), R.color.rating_medium)
            else -> ContextCompat.getColor(requireContext(), R.color.rating_low)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


