package com.vimal.dependencyinjection.model.artist

import com.google.gson.annotations.SerializedName
import com.vimal.dependencyinjection.model.artist.Artist

data class PopularArtistList(
    @SerializedName("results")
    val results: List<ArtistDto>,
)
