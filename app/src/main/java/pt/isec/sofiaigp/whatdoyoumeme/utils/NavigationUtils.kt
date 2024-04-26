package pt.isec.sofiaigp.whatdoyoumeme.utils

import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.screens.CreateGameScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.FindGameScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.GameRulesScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.HomePageScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.WaitingRoomScreenTTTTT


@Composable
fun NavigationGraph(navController: NavHostController, viewModel: GameViewModel) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "Home Screen") {
        composable("Home Screen") { HomePageScreen(navController, viewModel) }
        composable("Create Game/{userName}") {backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            CreateGameScreen(navController, viewModel, userName)
        }
        composable("Game Rules") { GameRulesScreen(context, navController) }
        composable("Find Game/{userName}") {backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            FindGameScreen(navController, viewModel, userName)
        }
        composable("Waiting Room/{roomName}"){backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            WaitingRoomScreenTTTTT(viewModel, roomName)
        }
    }
}