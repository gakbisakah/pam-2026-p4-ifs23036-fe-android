package org.delcom.pam_p4_ifs23036.network.plants.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: String? = null,
    val title: String,
    val director: String,
    val releaseYear: Int,
    val genre: String,
    val rating: Double,
    val description: String? = "",
    val posterPath: String? = "",
    val createdAt: String? = null,
    val updatedAt: String? = null
)
