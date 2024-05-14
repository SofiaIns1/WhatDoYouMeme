package pt.isec.sofiaigp.whatdoyoumeme.screens.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.sofiaigp.whatdoyoumeme.data.GameRoom
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.screens.player.Card
import pt.isec.sofiaigp.whatdoyoumeme.screens.player.MemeCard
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue

@Composable
fun ChoseWinnerScreen(
    navController: NavController, viewModel: GameViewModel, roomName: String, userName: String
) {
    viewModel.getGameRoomByName(roomName)

    val gameRoom = viewModel.gameRoom.observeAsState()
    val players = viewModel.players.observeAsState()

    var clickable = ""

    val roomId = gameRoom.value?.roomId

    if (roomId != null) {
        if (viewModel.isJudge(roomId, userName)) {
            clickable = "yes"
        }

    }

//    var chosenMeme by remember {
//        mutableStateOf("")
//    }

//    if (roomId != null) {
//        viewModel.getChosenMeme(roomId) {
//            chosenMeme = it
//        }
//    }
   val chosenMeme = gameRoom.value?.chosenMeme.toString()


    var i = 0

    var sentences by remember { mutableStateOf(emptyList<String>()) }

    if (roomId != null) {
        viewModel.getSelectedCards(roomId) { cards ->
            sentences = cards
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
            .background(Color.White)
            .padding(10.dp)
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
                    color = DarkBlue,
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
                            /*TODO: check if gameRoom.value?.players!![player] is not this screen's player*/
                            Text(
                                text = players.value!![player].username
                                        + " = " + players.value!![player].score,
                                color = Color.DarkGray,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            if (players.value?.size != null && players.value?.size!! > 3) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        if (roomId != null) {
                            sentences.getOrNull(i)?.let {
                                Card(
                                    sentence = it,
                                    memeUrl = clickable,
                                    roomName = roomName,
                                    navController = navController,
                                    viewModel,
                                    userName,
                                    roomId,
                                    true
                                )
                            }
                        }
                        i++
                    }

                    if (players.value?.size!! > 4) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                        ) {
                            if (roomId != null) {
                                sentences.getOrNull(i)?.let {
                                    Card(
                                        sentence = it,
                                        memeUrl = clickable,
                                        roomName = roomName,
                                        navController = navController,
                                        viewModel,
                                        userName,
                                        roomId,
                                        true
                                    )
                                }
                            }
                            i++
                        }
                    }

                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (players.value?.size != null && players.value?.size!! % 2 == 0) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        if (roomId != null) {
                            sentences.getOrNull(i)?.let {
                                Card(
                                    sentence = it,
                                    memeUrl = clickable,
                                    roomName = roomName,
                                    navController = navController,
                                    viewModel,
                                    userName,
                                    roomId,
                                    true
                                )
                            }
                        }
                        i++
                    }
                }
                MemeCard(memeUrl = chosenMeme)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    if (roomId != null) {
                        sentences.getOrNull(i)?.let {
                            Card(
                                sentence = it,
                                memeUrl = clickable,
                                roomName = roomName,
                                navController = navController,
                                viewModel,
                                userName,
                                roomId,
                                true
                            )
                        }
                    }
                    i++
                }
                if (players.value?.size != null && players.value?.size!! != 4) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        if (roomId != null) {
                            sentences.getOrNull(i)?.let {
                                Card(
                                    sentence = it,
                                    memeUrl = clickable,
                                    roomName = roomName,
                                    navController = navController,
                                    viewModel,
                                    userName,
                                    roomId,
                                    true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}