package com.example.cinemashift.presentation.screen.moviedeatail

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.i18n.DateTimeFormatter
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val args: MovieDetailFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModels()
    private var isDescriptionExpanded = false

    private val selectedButtons = mutableMapOf<String, MaterialButton?>()

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
        setupObservers()
        viewModel.loadMovie(args.movieId)
    }

    private fun setupClickListeners() {
        with(binding) {
            backButton.setOnClickListener {
                NavHostFragment.findNavController(this@MovieDetailFragment).popBackStack()
            }

            expandButton.setOnClickListener {
                toggleDescription()
            }
        }
    }

    private fun setupObservers() {
        viewModel.movie.observe(viewLifecycleOwner, ::bindMovieData)
        viewModel.schedule.observe(viewLifecycleOwner, ::setupDaysTabs)
        viewModel.error.observe(viewLifecycleOwner, ::showError)
    }

    private fun toggleDescription() {
        isDescriptionExpanded = !isDescriptionExpanded
        val textView = binding.descriptionTextView
        val expandButton = binding.expandButton

        TransitionManager.beginDelayedTransition(binding.root as ViewGroup)

        if (isDescriptionExpanded) {
            textView.maxLines = Integer.MAX_VALUE
            expandButton.text = getString(R.string.button_collapse)
        } else {
            textView.maxLines = 3
            expandButton.text = getString(R.string.button_expand)
        }
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

            descriptionTextView.apply {
                text = movie.description
                maxLines = 3
                ellipsize = TextUtils.TruncateAt.END
            }

            expandButton.apply {
                isVisible = movie.description.length > 100
                text = getString(R.string.button_expand)
            }

            typeTextView.text = getString(R.string.movie_type_film)
            ratingBar.rating = movie.rating / 2
            kinopoiskRatingText.text = getString(R.string.kinopoisk_rating_format, movie.rating)

            val ratingColor = getRatingColor(movie.rating)
            ratingBar.progressTintList = ColorStateList.valueOf(ratingColor)
            ratingBar.progressBackgroundTintList = ColorStateList.valueOf(ratingColor)

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
            val tab = binding.daysTabLayout.newTab()
            val customView = layoutInflater.inflate(R.layout.item_tab_date, null)

            val dayTextView = customView.findViewById<TextView>(R.id.dayOfWeekText)
            val dateTextView = customView.findViewById<TextView>(R.id.dateText)

            val (dayOfWeek, date) = formatDate(schedule.date)
            dayTextView.text = dayOfWeek
            dateTextView.text = date

            tab.customView = customView
            binding.daysTabLayout.addTab(tab)
        }

        if (schedules.isNotEmpty()) {
            showSeancesForDay(schedules[0])
        }

        binding.daysTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                schedules.getOrNull(tab?.position ?: 0)?.let { showSeancesForDay(it) }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }


    private fun formatDate(date: String): Pair<String, String> {
        val dateFormatter = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        val parsedDate = dateFormatter.parse(date)
        val calendar = Calendar.getInstance().apply { time = parsedDate!! }

        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Пн"
            Calendar.TUESDAY -> "Вт"
            Calendar.WEDNESDAY -> "Ср"
            Calendar.THURSDAY -> "Чт"
            Calendar.FRIDAY -> "Пт"
            Calendar.SATURDAY -> "Сб"
            Calendar.SUNDAY -> "Вс"
            else -> ""
        }

        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val monthName = getMonthShortName(calendar.get(Calendar.MONTH) + 1)

        return Pair(dayOfWeek, "$dayOfMonth $monthName")
    }

    private fun getMonthShortName(month: Int): String = when (month) {
        1 -> "янв"
        2 -> "фев"
        3 -> "мар"
        4 -> "апр"
        5 -> "май"
        6 -> "июн"
        7 -> "июл"
        8 -> "авг"
        9 -> "сен"
        10 -> "окт"
        11 -> "ноя"
        12 -> "дек"
        else -> ""
    }

    private fun showSeancesForDay(schedule: Schedule) {
        binding.hallsContainer.removeAllViews()

        schedule.seances.groupBy { it.hall.name }.forEach { (hallName, seances) ->
            addHallTitle(hallName)
            addSeancesButtons(hallName, seances)
        }
    }



    private fun addHallTitle(hallName: String) {
        TextView(requireContext()).apply {
            text = hallName
            setTextAppearance(com.google.android.material.R.style.TextAppearance_MaterialComponents_Subtitle1)
            setTextColor(ContextCompat.getColor(context, R.color.gray))
            setPadding(0, 16, 0, 8)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }.also { binding.hallsContainer.addView(it) }
    }




    private fun addSeancesButtons(hallName: String, seances: List<Seance>) {
        val flexboxLayout = FlexboxLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            flexWrap = FlexWrap.WRAP
            justifyContent = JustifyContent.FLEX_START
        }

        seances.forEach { seance ->
            val button = context?.let {
                MaterialButton(
                    it,
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

                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                    setTextColor(ContextCompat.getColor(context, R.color.gray))

                    setOnClickListener {
                        val currentSelected = selectedButtons[hallName]

                        if (currentSelected == this) {
                            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                            setTextColor(ContextCompat.getColor(context, R.color.gray))
                            selectedButtons[hallName] = null
                        } else {

                            currentSelected?.apply {
                                backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                                setTextColor(ContextCompat.getColor(context, R.color.gray))
                            }

                            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray))
                            setTextColor(ContextCompat.getColor(context, R.color.white))

                            selectedButtons[hallName] = this
                        }
                    }
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



    private fun showError(error: String) {
        if (error.isNotEmpty()) {
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

