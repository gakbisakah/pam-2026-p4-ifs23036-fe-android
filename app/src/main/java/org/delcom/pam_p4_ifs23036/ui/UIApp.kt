package org.delcom.pam_p4_ifs23036.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23036.helper.ConstHelper
import org.delcom.pam_p4_ifs23036.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23036.ui.screens.*
import org.delcom.pam_p4_ifs23036.ui.viewmodels.MovieViewModel
import org.delcom.pam_p4_ifs23036.ui.viewmodels.PlantViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController: NavHostController = rememberNavController(),
    plantViewModel: PlantViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val movieViewModel: MovieViewModel = hiltViewModel()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState){ snackbarData ->
            CustomSnackbar(snackbarData, onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() })
        } },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))

        ) {
            composable(route = ConstHelper.RouteNames.Home.path) {
                HomeScreen(navController = navController)
            }

            composable(route = ConstHelper.RouteNames.Profile.path) {
                ProfileScreen(navController = navController, plantViewModel = plantViewModel)
            }

            composable(route = ConstHelper.RouteNames.Plants.path) {
                PlantsScreen(navController = navController, plantViewModel = plantViewModel)
            }

            composable(route = ConstHelper.RouteNames.PlantsAdd.path) {
                PlantsAddScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel)
            }

            composable(
                route = ConstHelper.RouteNames.PlantsDetail.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
                PlantsDetailScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel, plantId = plantId)
            }

            composable(
                route = ConstHelper.RouteNames.PlantsEdit.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
                PlantsEditScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel, plantId = plantId)
            }

            // Movie Routes
            composable(route = ConstHelper.RouteNames.Movies.path) {
                MoviesScreen(navController = navController, movieViewModel = movieViewModel)
            }

            composable(route = ConstHelper.RouteNames.MoviesAdd.path) {
                // Fixed: Removed snackbarHost parameter as it's no longer used in MoviesAddScreen
                MoviesAddScreen(navController = navController, movieViewModel = movieViewModel)
            }

            composable(
                route = ConstHelper.RouteNames.MoviesEdit.path,
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                MoviesEditScreen(navController = navController, movieViewModel = movieViewModel, movieId = movieId)
            }
        }
    }
}
