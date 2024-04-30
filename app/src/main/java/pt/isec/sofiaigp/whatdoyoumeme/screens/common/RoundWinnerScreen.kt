package pt.isec.sofiaigp.whatdoyoumeme.screens.common

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
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.screens.player.Card
import pt.isec.sofiaigp.whatdoyoumeme.screens.player.MemeCard
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.Lilac

@Composable
fun RoundWinnerScreen(navController: NavController, viewModel : GameViewModel, roomName : String, userName:String) {
    val gameRoom = viewModel.getGameRoomByName(roomName).observeAsState()
    val roomId = gameRoom.value?.roomId
    val isJudge = roomId?.let { viewModel.isJudge(it) }
    val sentences = listOf("When you lie down for a quick nap and wake up 8 hours later",
        "When you lie down for a quick nap and wake up 8 hours later",
        "When you lie down for a quick nap and wake up 8 hours later",
        "When you lie down for a quick nap and wake up 8 hours later",
        "When you lie down for a quick nap and wake up 8 hours later")

    var score by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(roomId) {
        if (roomId != null) {
            viewModel.getPlayerScore(roomId, userName){playerScore ->
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
    ){
        Row(
            modifier = Modifier
            .fillMaxWidth()
        ){
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
                    gameRoom.value?.players?.size?.let {
                        items(it) { player ->
                            /*TODO: check if gameRoom.value?.players!![player] is not this screens player*/
                            Text(
                                text = gameRoom.value?.players!![player].username
                                        + " = " + gameRoom.value?.players!![player].score,
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
        ){
            if (roomId != null) {
                Card(sentences.getOrNull(4) ?: "", "", roomName, navController, viewModel, userName, roomId)
            } /*TODO: change to winner sentence*/

            Spacer(modifier = Modifier.height(20.dp))

            MemeCard(memeUrl = "not empty") /*TODO: send the URL to the judge's chosen meme*/
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                      /*TODO: go to next round
                      */
                if(isJudge == true){
                    navController.navigate("Judge Screen/${roomName}/$userName")
                }
                else{
                    navController.navigate("Player Screen/${roomName}/$userName")

                }
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
                if (roomId != null) {
                    viewModel.deletePlayer(roomId, userName)
                }
                navController.navigate("Home Screen")
            }
        )
    }
}