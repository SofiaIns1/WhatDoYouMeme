package pt.isec.sofiaigp.whatdoyoumeme.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.screens.common.CreateGameScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.common.FindGameScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.common.GameRulesScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.common.HomePageScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.judge.JudgeScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.judge.JudgeWaitScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.common.WaitingRoomScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.common.WinnerScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.player.PlayerScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.common.ChoseWinnerScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.common.RoundWinnerScreen


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
        composable("Waiting Room/{roomName}/{userName}") {backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""

            WaitingRoomScreen(navController, roomName, userName, viewModel)
        }
        composable("Winner Screen/{roomName}") {backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            WinnerScreen(navController, viewModel, roomName)
        }
        //Judge
        composable("Judge Screen/{roomName}") {backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""

            JudgeScreen(navController, viewModel, roomName, userName)
        }
        composable("Judge Wait/{roomName}/{imageURL}") {backStackEntry ->
           val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val imageURL = backStackEntry.arguments?.getString("imageURL") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""

            JudgeWaitScreen(viewModel, roomName, imageURL, userName)
        }
        //Player
        composable("Player Screen/{roomName}/{userName}") {backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""

            PlayerScreen(navController, viewModel, roomName, userName)
        }
        composable("Chose Winner/{roomName}/{userName}") {backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""

            ChoseWinnerScreen(navController, viewModel, roomName, userName)
        }
        composable("Show Winner/{roomName}/{userName}") {backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""

            RoundWinnerScreen(navController, viewModel, roomName, userName)
        }
    }
}