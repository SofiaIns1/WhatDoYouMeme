package pt.isec.sofiaigp.whatdoyoumeme

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import pt.isec.sofiaigp.whatdoyoumeme.screens.CreateGameScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.GameRulesScreen
import pt.isec.sofiaigp.whatdoyoumeme.screens.HomePageScreen
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.WhatDoYouMemeTheme
import pt.isec.sofiaigp.whatdoyoumeme.utils.NavigationGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatDoYouMemeTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentRoute = remember { mutableStateOf("") }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentRoute.value = destination.route ?: ""
        }

    }

    NavigationGraph(navController)
}
