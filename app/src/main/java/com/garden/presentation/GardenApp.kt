package com.garden.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import com.garden.common.Constant
import com.garden.presentation.home.GardenPage
import com.garden.presentation.home.HomeScreen
import com.garden.presentation.plantdetail.PlantDetailsScreen
import com.garden.presentation.viewmodels.HomeViewModel

@Composable
fun GardenApp(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    GardenNavHost(
        homeViewModel = homeViewModel,
        navController = navController,
    )
}

@Composable
fun GardenNavHost(
    navController: NavHostController,
    onPageChange: (GardenPage) -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val activity = (LocalContext.current as Activity)
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onPlantClick = {
                    navController.navigate("plantDetail/${it.id}")
                },
                onPageChange = onPageChange,
                homeViewModel = homeViewModel
            )
        }
        composable(
            "plantDetail/{plantId}",
            arguments = listOf(navArgument("plantId") {
                type = NavType.IntType
            })
        ) {
            PlantDetailsScreen(
                onBackClick = { navController.navigateUp() },
                onShareClick = {
                    createShareIntent(activity, it)
                },
                onSupportCallClick = {
                    createCallIntent(activity)
                }
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

private fun createCallIntent(activity: Activity) {
    val u = Uri.parse("tel:" + Constant.SUPPORT_PHONE_NUMBER)

    // Create the intent and set the data for the
    // intent as the phone number.
    val i = Intent(Intent.ACTION_DIAL, u)
    try {

        // Launch the Phone app's dialer with a phone
        // number to dial a call.
        activity.startActivity(i)
    } catch (s: SecurityException) {

        // show() method display the toast with
        // exception message.
        Toast.makeText(activity, "An error occurred", Toast.LENGTH_LONG)
            .show()
    }
}