package com.example.gloryflyerapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gloryflyerapp.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("otp_verification/{verificationId}/{name?}") { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
            val name = backStackEntry.arguments?.getString("name")
            OtpVerificationScreen(navController, verificationId, name)
        }
        composable("home") { HomeScreen(navController) }
        composable("create_event") { CreateEventScreen(navController) }
        composable("preview_flyer/{eventId}") { backStackEntry ->
            FlyerPreviewScreen(
                navController = navController,
                eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            )
        }
        composable("events_list") { EventsListScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("saved_flyer") { SavedFlyerScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    // TODO: Implement UI
}

@Composable
fun SignupScreen(navController: NavHostController) {
    // TODO: Implement UI
}

@Composable
fun OtpVerificationScreen(navController: NavHostController, verificationId: String, name: String?) {
    // TODO: Implement UI
}

@Composable
fun GreetingScreen(navController: NavHostController) {
    // TODO: Implement UI
}

@Composable
fun HomeScreen(navController: NavHostController) {
    // TODO: Implement UI
}

@Composable
fun EventsListScreen(navController: NavHostController) {
    // TODO: Implement UI
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    // TODO: Implement UI
}

@Composable
fun SavedFlyerScreen(navController: NavHostController) {
    // TODO: Implement UI
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    // TODO: Implement UI
} 