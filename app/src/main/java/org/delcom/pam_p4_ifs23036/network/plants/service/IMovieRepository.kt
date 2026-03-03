package org.delcom.pam_p4_ifs23036.network.plants.service

import org.delcom.pam_p4_ifs23036.network.plants.data.Movie
import java.io.File

interface IMovieRepository {
    suspend fun getAllMovies(): List<Movie>
    suspend fun getMovieById(id: String): Movie
    suspend fun createMovie(movie: Movie, posterFile: File? = null): Movie
    suspend fun updateMovie(id: String, movie: Movie): Map<String, String>
    suspend fun deleteMovie(id: String): Map<String, String>
}
