package pt.isec.sofiaigp.whatdoyoumeme.screens.judge

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import pt.isec.sofiaigp.whatdoyoumeme.R
import pt.isec.sofiaigp.whatdoyoumeme.data.GameRoom
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue

@OptIn(ExperimentalCoilApi::class)
@Composable
fun JudgeWaitScreen(
    navController: NavHostController,
    viewModel: GameViewModel,
    roomName: String,
    userName: String
) {
    viewModel.getGameRoomByName(roomName)

    val gameRoom = viewModel.gameRoom.observeAsState()
    val players = viewModel.players.observeAsState()

    val roomId = gameRoom.value?.roomId
    var isJudge by remember { mutableStateOf(false) }


    if (roomId != null) {
        viewModel.hasGameStarted(roomId) {
            if (viewModel.isJudge(roomId, userName)) {
                isJudge = true
            }
        }
    }

    var chosenMeme by remember {
        mutableStateOf("")
    }

    if (roomId != null) {
        viewModel.getChosenMeme(roomId) {
            chosenMeme = it
        }
    }


    if (roomId != null) {
        if(viewModel.allCardsSelected(roomId, userName)){
            navController.navigate("Chose Winner/${roomName}/$userName")
        }
    }


    var score by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(roomId) {
        if (roomId != null) {
            viewModel.getPlayerScore(roomId, userName) { playerScore ->
                score = playerScore
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(DarkBlue)
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Score = $score",
                    color = Color.White,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Start
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.End
                ) {
                    players.value?.size?.let {
                        items(it) { player ->
                            if (!isJudge) {
                                Text(
                                    text = players.value!![player].username
                                            + " = " + players.value!![player].score,
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }

                        }
                    }
                }

            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Waiting for the other players...",
                color = Color.White,
                fontSize = 25.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(15.dp))

            val painter = rememberImagePainter(
                data = chosenMeme
            )
            Image(
                painter = painter,
                contentDescription = "WDYM logo",
                modifier = Modifier
                    .size(280.dp)
            )

        }
    }
}