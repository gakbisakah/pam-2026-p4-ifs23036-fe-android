package org.delcom.pam_p4_ifs23036.network.plants.service

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.delcom.pam_p4_ifs23036.network.plants.data.Movie
import java.io.File
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApiService: MovieApiService
) : IMovieRepository {
    override suspend fun getAllMovies(): List<Movie> = movieApiService.getAllMovies()
    override suspend fun getMovieById(id: String): Movie = movieApiService.getMovieById(id)
    
    override suspend fun createMovie(movie: Movie, posterFile: File?): Movie {
        val title = movie.title.toRequestBody("text/plain".toMediaTypeOrNull())
        val director = movie.director.toRequestBody("text/plain".toMediaTypeOrNull())
        val releaseYear = movie.releaseYear.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val genre = movie.genre.toRequestBody("text/plain".toMediaTypeOrNull())
        val rating = movie.rating.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val description = (movie.description ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
        
        val posterPart = posterFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("poster", it.name, requestFile)
        }
        
        return movieApiService.createMovie(
            title, director, releaseYear, genre, rating, description, posterPart
        )
    }

    override suspend fun updateMovie(id: String, movie: Movie): Map<String, String> = movieApiService.updateMovie(id, movie)
    override suspend fun deleteMovie(id: String): Map<String, String> = movieApiService.deleteMovie(id)
}
