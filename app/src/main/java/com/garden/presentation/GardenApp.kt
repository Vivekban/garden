package com.garden.presentation

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.garden.R
import com.garden.presentation.home.GardenPage
import com.garden.presentation.home.HomeScreen
import com.garden.presentation.plantdetail.PlantDetailsScreen
import com.garden.presentation.viewmodels.PlantListViewModel

@Composable
fun GardenApp(
    plantListViewModel: PlantListViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    GardenNavHost(
        plantListViewModel = plantListViewModel,
        navController = navController,
    )
}

@Composable
fun GardenNavHost(
    navController: NavHostController,
    onPageChange: (GardenPage) -> Unit = {},
    plantListViewModel: PlantListViewModel = hiltViewModel(),
) {
    val activity = (LocalContext.current as Activity)
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onPlantClick = {
                    navController.navigate("plantDetail/${it.id}")
                },
                onPageChange = onPageChange,
                plantListViewModel = plantListViewModel
            )
        }
        composable(
            "plantDetail/{plantId}",
            arguments = listOf(navArgument("plantId") {
                type = NavType.StringType
            })
        ) {
            PlantDetailsScreen(
                onBackClick = { navController.navigateUp() },
                onShareClick = {
                    createShareIntent(activity, it)
                },
            )
        }
    }
}

// Helper function for calling a share functionality.
// Should be used when user presses a share button/menu item.
private fun createShareIntent(activity: Activity, plantName: String) {
    val shareText = activity.getString(R.string.share_text_plant, plantName)
    val shareIntent = ShareCompat.IntentBuilder(activity)
        .setText(shareText)
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    activity.startActivity(shareIntent)
}