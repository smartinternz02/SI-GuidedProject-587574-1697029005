package com.vit.snacksquad

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable(route = "LoginScreen") {
            LoginScreen(navController = navController)
        }
        composable(route = "SignUpScreen") {
            SignUpScreen(navController = navController)
        }
        composable(route = "RestaurantList") {
            RestaurantList(navController = navController)
        }
        composable(
            route = "Menu" + "/{restaurant}",
            arguments = listOf(navArgument("restaurant") {
                type = NavType.StringType
                nullable = false
            })
        ) { entry ->
            entry.arguments?.getString("restaurant")?.let {
                Menu(
                    navController = navController,
                    restaurant = it
                )
            }
        }
        composable(
            route = "PaymentPage" + "/{restaurant}",
            arguments = listOf(navArgument("restaurant") { type = NavType.StringType })
        ) { entry ->
            PaymentPage(
                navController = navController,
                restaurant = entry.arguments?.getString("restaurant")
            )
        }
    }
}