package com.example.gloryflyerapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gloryflyerapp.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        
        composable("login") {
            LoginScreen(navController)
        }
        
        composable("signup") {
            SignupScreen(navController)
        }
        
        composable("otp_verification/{verificationId}") { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
            OtpVerificationScreen(navController, verificationId)
        }
        
        composable("home") {
            HomeScreen(navController)
        }
        
        composable("profile") {
            ProfileScreen(navController)
        }
        
        composable("edit_profile") {
            EditProfileScreen(navController)
        }
        
        composable("settings") {
            SettingsScreen(navController)
        }
        
        composable("privacy_settings") {
            PrivacySettingsScreen(navController)
        }
        
        composable("help_support") {
            HelpSupportScreen(navController)
        }
        
        composable("create_event") {
            CreateEventScreen(navController)
        }
        
        composable("events_list") {
            EventsListScreen(navController)
        }
        
        composable("preview_flyer/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            FlyerPreviewScreen(navController, eventId)
        }

        composable("edit_event/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EditEventScreen(navController, eventId)
        }
    }
} 