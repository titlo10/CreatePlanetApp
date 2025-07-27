package com.example.createplanetapp

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.createplanetapp.pages.ItemMainPage
import com.example.createplanetapp.ui.theme.LocalNavController
import com.google.firebase.FirebaseApp
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel : AuthViewModel by viewModels()
        FirebaseApp.initializeApp(this)
        Thread.sleep(2000)
        installSplashScreen()
        GlobalData.initialize(this)
        setContent {
            val navController = rememberNavController()

            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                NavHost(navController = navController, startDestination = "Main_Screen") {
                    composable("Main_Screen") {
                        MainScreen(authViewModel)
                    }
                    composable(
                        "ItemMainPage/{title}",
                        arguments = listOf(navArgument("title") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val title = backStackEntry.arguments?.getString("title")!!
                        ItemMainPage(title) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
