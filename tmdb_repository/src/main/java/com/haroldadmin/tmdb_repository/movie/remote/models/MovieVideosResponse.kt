package com.haroldadmin.tmdb_repository.movie.remote.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MovieVideosResponse(
    @Json(name="id") val id: Int,
    @Json(name="results") val results: List<MovieVideo>): Parcelable
