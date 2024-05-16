package pt.isec.sofiaigp.whatdoyoumeme.screens.common

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
fun RoundWinnerScreen(
    navController: NavController,
    viewModel: GameViewModel,
    roomName: String,
    userName: String,
    sentence: String
) {
    viewModel.getGameRoomByName(roomName)

    val gameRoom = viewModel.gameRoom.observeAsState()
    val players = viewModel.players.observeAsState()


    val roomId = gameRoom.value?.roomId

    if (roomId != null) {
        gameRoom.value?.score?.size?.toString()?.let { Log.i("1", it) }
        //Log.i("1", gameRoom.value?.score?.get(players.value.get(0).username)?.toString()
        //Log.i("1", gameRoom.value?.score?.get(0).toString())
       //viewModel.updateScore(sentence, roomId)
    }

    var winner by remember {
        mutableStateOf("")
    }

    val memeUrl = gameRoom.value?.chosenMeme

    var score by remember {
        mutableIntStateOf(0)
    }


    if (roomId != null) {
        Log.i("ss", score.toString())
        LaunchedEffect(roomId, userName) {
            viewModel.getPlayerScore(roomId, userName) { playerScore ->
                score = playerScore
            }
        }
        /*viewModel.getPlayerScore(roomId, userName) { playerScore ->
            score = playerScore
        }*/

    }

    Log.i("s", score.toString())

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
                            /*TODO: check if gameRoom.value?.players!![player] is not this screens player*/
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
                .weight(1f)
        ) {
            if (roomId != null) {
                if (memeUrl != null) {
                    Card(
                        sentence,
                        memeUrl,
                        roomName,
                        navController,
                        viewModel,
                        userName,
                        roomId
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (memeUrl != null) {
                MemeCard(memeUrl)
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
//                if (roomId != null) {
//                    viewModel.resetMeme(roomId)
//                }
//                if (roomId != null) {
//                    viewModel.resetSelectedCards(roomId)
//                }
//
//                if (roomId != null) {
//                    viewModel.selectJudge(roomId)
//                }
//
//                if (roomId != null) {
//                    if (viewModel.isJudge(roomId, userName)) {
//                        navController.navigate("Judge Screen/${roomName}/$userName")
//                    } else
//                        navController.navigate("Player Screen/${roomName}/$userName")

//                }


            },
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Lilac,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "NEXT ROUND",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
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
