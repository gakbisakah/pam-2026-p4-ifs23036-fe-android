package org.delcom.pam_p4_ifs23036.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.delcom.pam_p4_ifs23036.network.plants.data.Movie
import org.delcom.pam_p4_ifs23036.ui.viewmodels.MovieViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesAddScreen(
    navController: NavHostController,
    movieViewModel: MovieViewModel,
) {
    var title by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> selectedImageUri = uri }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Movie", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Selected Poster",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.AddAPhoto, contentDescription = "Add Photo", tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.size(48.dp))
                                    Text("Tap to add a poster", color = MaterialTheme.colorScheme.onSecondaryContainer)
                                }
                            }
                        }

                        OutlinedTextField(
                            value = title, 
                            onValueChange = { title = it }, 
                            label = { Text("Title") }, 
                            modifier = Modifier.fillMaxWidth(), 
                            leadingIcon = { Icon(Icons.Default.Title, null) }
                        )
                        OutlinedTextField(
                            value = director, 
                            onValueChange = { director = it }, 
                            label = { Text("Director") }, 
                            modifier = Modifier.fillMaxWidth(), 
                            leadingIcon = { Icon(Icons.Default.Person, null) }
                        )
                        OutlinedTextField(
                            value = year, 
                            onValueChange = { year = it }, 
                            label = { Text("Year") }, 
                            modifier = Modifier.fillMaxWidth(), 
                            leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = genre, 
                            onValueChange = { genre = it }, 
                            label = { Text("Genre") }, 
                            modifier = Modifier.fillMaxWidth(), 
                            leadingIcon = { Icon(Icons.Default.Movie, null) }
                        )
                        OutlinedTextField(
                            value = rating, 
                            onValueChange = { rating = it }, 
                            label = { Text("Rating (e.g., 8.5)") }, 
                            modifier = Modifier.fillMaxWidth(), 
                            leadingIcon = { Icon(Icons.Default.Star, null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        OutlinedTextField(
                            value = description, 
                            onValueChange = { description = it }, 
                            label = { Text("Description") }, 
                            modifier = Modifier.fillMaxWidth(), 
                            minLines = 3, 
                            leadingIcon = { Icon(Icons.Default.Description, null) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (title.isBlank() || director.isBlank() || year.isBlank()) {
                            Toast.makeText(context, "Title, Director and Year are required", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        isLoading = true
                        scope.launch {
                            val posterFile = selectedImageUri?.let { uri ->
                                withContext(Dispatchers.IO) {
                                    try {
                                        val inputStream = context.contentResolver.openInputStream(uri)
                                        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
                                        inputStream?.use { input ->
                                            FileOutputStream(file).use { output ->
                                                input.copyTo(output)
                                            }
                                        }
                                        file
                                    } catch (e: Exception) {
                                        null
                                    }
                                }
                            }
                            
                            val movie = Movie(
                                title = title,
                                director = director,
                                releaseYear = year.toIntOrNull() ?: 0,
                                genre = genre,
                                rating = rating.toDoubleOrNull() ?: 0.0,
                                description = description,
                                posterPath = ""
                            )
                            
                            movieViewModel.addMovie(movie, posterFile) { success ->
                                isLoading = false
                                if (success) {
                                    Toast.makeText(context, "Movie added successfully", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(context, "Failed to add movie. Check Connection or Server.", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text("Save Movie", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
