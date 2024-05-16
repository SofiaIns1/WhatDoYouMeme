package pt.isec.sofiaigp.whatdoyoumeme.screens.player

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import pt.isec.sofiaigp.whatdoyoumeme.R
import pt.isec.sofiaigp.whatdoyoumeme.data.GameRoom
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.screens.Card
import pt.isec.sofiaigp.whatdoyoumeme.screens.MemeCard
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.LightBlue
import kotlin.random.Random

@Composable
fun PlayerScreen(
    navController: NavController,
    viewModel: GameViewModel,
    roomName: String,
    userName: String
) {
    viewModel.getGameRoomByName(roomName)

    val gameRoom = viewModel.gameRoom.observeAsState()
    val players = viewModel.players.observeAsState()
    val sentences = viewModel.playableCards.observeAsState()


    LaunchedEffect(Unit) {
        viewModel.fetchPlayableCards()
    }

    var memeUrl by remember { mutableStateOf("n") }

    val roomId = gameRoom.value?.roomId

    var chosenMeme by remember {
        mutableStateOf("")
    }

    if (roomId != null) {
        viewModel.getChosenMeme(roomId) {
            chosenMeme = it
        }
    }

    if (roomId != null) {
        viewModel.isCardSelected(userName, roomId) {
            navController.navigate("Chose Winner/${roomName}/$userName")
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
                    text = "Score = $score ",
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
                            players.value!![player].username?.let { it1 ->
                                Text(
                                    text = it1
                                            + " = " + players.value!![player].score,
                                    color = Color.DarkGray,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until 3) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color.White)
                        .padding(10.dp)
                        .weight(1f)
                ) {
                    if (roomId != null) {
                        Card(
                            sentences.value?.getOrNull(i) ?: "",
                            memeUrl, roomName, navController, viewModel, userName, roomId
                        )
                    }
                    if (i < 2) {
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color.White)
                    .padding(10.dp)
                    .weight(1f)
            ) {
                if (roomId != null) {
                    Card(
                        sentences.value?.getOrNull(3) ?: "",
                        memeUrl, roomName, navController, viewModel, userName, roomId
                    )

                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                MemeCard(chosenMeme)
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(Color.White)
                    .padding(10.dp)
                    .weight(1f)
            ) {
                if (roomId != null) {
                    Card(
                        sentences.value?.getOrNull(4) ?: "",
                        memeUrl, roomName, navController, viewModel, userName, roomId
                    )

                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 5 until 7) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color.White)
                        .padding(10.dp)
                        .weight(1f)
                ) {
                    if (roomId != null) {
                        Card(
                            sentences.value?.getOrNull(i) ?: "",
                            memeUrl, roomName, navController, viewModel, userName, roomId
                        )

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        ClickableText(
            text = AnnotatedString("Leave party"),
            style = TextStyle(
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline,
                fontSize = 10.sp
            ),
            onClick = { offset ->
//                if (roomId != null) {
//                    viewModel.deletePlayer(roomId, userName)
//                }
                navController.navigate("Home Screen")
            }
        )
    }
}


