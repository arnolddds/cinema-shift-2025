package com.example.cinemashift.data.models

import com.example.cinemashift.domain.entity.Movie
import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("poster") val posterUrl: String
) {
    fun toDomain() = Movie(id, title, rating, posterUrl)
}
