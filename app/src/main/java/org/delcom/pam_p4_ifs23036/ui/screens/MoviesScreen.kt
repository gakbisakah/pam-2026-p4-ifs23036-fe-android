package org.delcom.pam_p4_ifs23036.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23036.helper.ConstHelper
import org.delcom.pam_p4_ifs23036.network.plants.data.Movie
import org.delcom.pam_p4_ifs23036.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23036.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23036.ui.viewmodels.MovieUiState
import org.delcom.pam_p4_ifs23036.ui.viewmodels.MovieViewModel

@Composable
fun MoviesScreen(
    navController: NavHostController,
    movieViewModel: MovieViewModel
) {
    Scaffold(
        topBar = { TopAppBarComponent(navController, "Movie Catalog", false) },
        bottomBar = { BottomNavComponent(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(ConstHelper.RouteNames.MoviesAdd.path) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Movie")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = movieViewModel.movieUiState) {
                is MovieUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is MovieUiState.Error -> Text("Error loading movies", modifier = Modifier.align(Alignment.Center))
                is MovieUiState.Success -> {
                    LazyColumn {
                        items(state.movies) { movie ->
                            MovieItem(movie, onEdit = {
                                navController.navigate(ConstHelper.RouteNames.MoviesEdit.path.replace("{movieId}", movie.id ?: ""))
                            }, onDelete = {
                                movieViewModel.deleteMovie(movie.id ?: "") {}
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
                Text(text = "Director: ${movie.director}")
                Text(text = "Year: ${movie.releaseYear}")
                Text(text = "Genre: ${movie.genre}")
                Text(text = "Rating: ${movie.rating}/10")
            }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Delete") }
        }
    }
}
