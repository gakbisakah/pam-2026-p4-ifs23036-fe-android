package org.delcom.pam_p4_ifs23036.network.plants.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23036.network.plants.data.Movie
import retrofit2.http.*

interface MovieApiService {
    @GET("movies")
    suspend fun getAllMovies(): List<Movie>

    @GET("movies/{id}")
    suspend fun getMovieById(@Path("id") id: String): Movie

    @Multipart
    @POST("movies")
    suspend fun createMovie(
        @Part("title") title: RequestBody,
        @Part("director") director: RequestBody,
        @Part("releaseYear") releaseYear: RequestBody,
        @Part("genre") genre: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part("description") description: RequestBody,
        @Part poster: MultipartBody.Part?
    ): Movie

    @PUT("movies/{id}")
    suspend fun updateMovie(@Path("id") id: String, @Body movie: Movie): Map<String, String>

    @DELETE("movies/{id}")
    suspend fun deleteMovie(@Path("id") id: String): Map<String, String>
}
