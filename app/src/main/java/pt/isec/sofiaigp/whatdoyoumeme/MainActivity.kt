package pt.isec.sofiaigp.whatdoyoumeme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.WhatDoYouMemeTheme
import pt.isec.sofiaigp.whatdoyoumeme.utils.NavigationGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: GameViewModel by viewModels()

        super.onCreate(savedInstanceState)
        setContent {
            WhatDoYouMemeTheme {
                MainScreen(viewModel)
            }
        }
    }
}


@Composable
fun MainScreen(viewModel: GameViewModel) {
    val navController = rememberNavController()
    val currentRoute = remember { mutableStateOf("") }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentRoute.value = destination.route ?: ""
        }

    }

    NavigationGraph(navController, viewModel)
}
