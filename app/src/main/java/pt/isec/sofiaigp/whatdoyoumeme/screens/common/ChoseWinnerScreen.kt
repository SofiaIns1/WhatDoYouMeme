package pt.isec.sofiaigp.whatdoyoumeme.screens.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.sofiaigp.whatdoyoumeme.data.GameRoom
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.screens.Card
import pt.isec.sofiaigp.whatdoyoumeme.screens.MemeCard
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.Lilac

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

    val chosenMeme = gameRoom.value?.chosenMeme.toString()

    var i = 0

    var sentences by remember { mutableStateOf(emptyList<Map<String, String>>()) }

    if (roomId != null) {
        viewModel.getSelectedCards(roomId) { cards ->
            sentences = cards
        }
    }


    var score by remember {
        mutableIntStateOf(0)
    }

    if (roomId != null) {
        viewModel.getPlayerScore(roomId, userName) { playerScore ->
            score = playerScore
        }
    }

    val isJudge = roomId?.let { viewModel.isJudge(it, userName) }


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
                            sentences.getOrNull(i)?.let { sentenceMap ->
                                val sentence = sentenceMap["card"]
                                Card(
                                    sentence = sentence.toString(),
                                    memeUrl = clickable,
                                    roomName = roomName,
                                    navController = navController,
                                    viewModel,
                                    userName,
                                    roomId,
                                    true
                                ){

                                        Log.d("Callback", "AQUI")

                                        navController.navigate("Show Winner/${roomName}/$userName/$sentence")

                                }
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
                                sentences.getOrNull(i)?.let { sentenceMap ->
                                    val sentence = sentenceMap["card"]
                                    Card(
                                        sentence = sentence.toString(),
                                        memeUrl = clickable,
                                        roomName = roomName,
                                        navController = navController,
                                        viewModel,
                                        userName,
                                        roomId,
                                        true
                                    ) {
                                            Log.d("Callback", "AQUI")
                                            navController.navigate("Show Winner/${roomName}/$userName/$sentence")

                                    }
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
                            sentences.getOrNull(i)?.let { sentenceMap ->
                                val sentence = sentenceMap["card"]
                                Card(
                                    sentence = sentence.toString(),
                                    memeUrl = clickable,
                                    roomName = roomName,
                                    navController = navController,
                                    viewModel,
                                    userName,
                                    roomId,
                                    true
                                ) {
                                    if (isJudge == false) {
                                        Log.d("Callback", "AQUI")

                                        navController.navigate("Show Winner/${roomName}/$userName/$sentence")
                                    }
                                }
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
                        sentences.getOrNull(i)?.let { sentenceMap ->
                            val sentence = sentenceMap["card"]
                            Card(
                                sentence = sentence.toString(),
                                memeUrl = clickable,
                                roomName = roomName,
                                navController = navController,
                                viewModel,
                                userName,
                                roomId,
                                true
                            ) {
                                if (isJudge == false) {
                                    Log.d("Callback", "AQUI")

                                    navController.navigate("Show Winner/${roomName}/$userName/$sentence")
                                }
                            }
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
                            sentences.getOrNull(i)?.let { sentenceMap ->
                                val sentence = sentenceMap["card"]
                                Card(
                                    sentence = sentence.toString(),
                                    memeUrl = clickable,
                                    roomName = roomName,
                                    navController = navController,
                                    viewModel,
                                    userName,
                                    roomId,
                                    true
                                ) {
                                    if (isJudge == false) {
                                        Log.d("Callback", "AQUI")

                                        navController.navigate("Show Winner/${roomName}/$userName/$sentence")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}