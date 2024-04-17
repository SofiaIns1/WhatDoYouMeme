package pt.isec.sofiaigp.whatdoyoumeme.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import pt.isec.sofiaigp.whatdoyoumeme.screens.CreateGameScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.GameRulesScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.HomePageScreen


@Composable
fun NavigationGraph(navController: NavHostController) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "Home Screen") {
        composable("Home Screen") { HomePageScreen(navController) }
        composable("Create Game") { CreateGameScreen(navController) }
        composable("Game Rules") { GameRulesScreen(context, navController) }
    }
}