package org.delcom.pam_p4_ifs23036.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.delcom.pam_p4_ifs23036.network.plants.data.Movie
import org.delcom.pam_p4_ifs23036.network.plants.service.IMovieRepository
import java.io.File
import javax.inject.Inject

sealed interface MovieUiState {
    data class Success(val movies: List<Movie>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: IMovieRepository
) : ViewModel() {
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    init {
        getMovies()
    }

    // Optimized: Added showLoading parameter to avoid flicker when updating data
    fun getMovies(showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                movieUiState = MovieUiState.Loading
            }
            try {
                val movies = movieRepository.getAllMovies()
                movieUiState = MovieUiState.Success(movies)
            } catch (e: Exception) {
                if (showLoading || movieUiState !is MovieUiState.Success) {
                    movieUiState = MovieUiState.Error
                }
            }
        }
    }

    fun addMovie(movie: Movie, posterFile: File?, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val result = movieRepository.createMovie(movie, posterFile)
                // Update local state directly or fetch without full screen loading
                getMovies(showLoading = false) 
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun updateMovie(id: String, movie: Movie, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                movieRepository.updateMovie(id, movie)
                getMovies(showLoading = false)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }

    fun deleteMovie(id: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                movieRepository.deleteMovie(id)
                getMovies(showLoading = false)
                onComplete(true)
            } catch (e: Exception) {
                onComplete(false)
            }
        }
    }
}
