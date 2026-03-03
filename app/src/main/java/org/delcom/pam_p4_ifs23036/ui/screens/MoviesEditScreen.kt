package org.delcom.pam_p4_ifs23036.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23036.network.plants.data.Movie
import org.delcom.pam_p4_ifs23036.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23036.ui.viewmodels.MovieUiState
import org.delcom.pam_p4_ifs23036.ui.viewmodels.MovieViewModel

@Composable
fun MoviesEditScreen(
    navController: NavHostController,
    movieViewModel: MovieViewModel,
    movieId: String
) {
    var title by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val uiState = movieViewModel.movieUiState
    LaunchedEffect(uiState) {
        if (uiState is MovieUiState.Success) {
            val movie = uiState.movies.find { it.id == movieId }
            if (movie != null) {
                title = movie.title
                director = movie.director
                year = movie.releaseYear.toString()
                genre = movie.genre
                rating = movie.rating.toString()
                description = movie.description ?: ""
            }
        }
    }

    Scaffold(
        topBar = { TopAppBarComponent(navController, "Edit Movie", true) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = director, onValueChange = { director = it }, label = { Text("Director") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = year, onValueChange = { year = it }, label = { Text("Year") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Genre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = rating, onValueChange = { rating = it }, label = { Text("Rating") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Button(
                onClick = {
                    val movie = Movie(
                        id = movieId,
                        title = title,
                        director = director,
                        releaseYear = year.toIntOrNull() ?: 0,
                        genre = genre,
                        rating = rating.toDoubleOrNull() ?: 0.0,
                        description = description,
                        posterPath = null
                    )
                    movieViewModel.updateMovie(movieId, movie) { success ->
                        if (success) navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Update Movie")
            }
        }
    }
}
