package com.example.cinemashift.data.model.response

import com.example.cinemashift.domain.entity.Schedule

data class ScheduleResponse(
    val success: Boolean,
    val reason: String?,
    val schedules: List<Schedule>
)
