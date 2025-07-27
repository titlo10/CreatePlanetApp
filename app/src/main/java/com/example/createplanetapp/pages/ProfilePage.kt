package com.example.createplanetapp.pages

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.createplanetapp.AuthState
import com.example.createplanetapp.AuthViewModel

@Composable
fun ProfilePage(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    val authState = authViewModel.authState.observeAsState()

    val startDestination = when (authState.value) {
        is AuthState.Authenticated -> "userProfile"
        else -> "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable("login") {
            LoginPage(navController, authViewModel)
        }
        composable("register") {
            RegisterPage(navController, authViewModel)
        }
        composable("userProfile") {
            UserProfilePage(navController, authViewModel)
        }
    }
}


