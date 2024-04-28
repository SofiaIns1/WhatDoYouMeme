package pt.isec.sofiaigp.whatdoyoumeme.screens.common

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.screens.player.Card
import pt.isec.sofiaigp.whatdoyoumeme.screens.player.MemeCard
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue

@Composable
fun ChoseWinnerScreen(navController: NavController, viewModel : GameViewModel, roomName : String) {
    val gameRoom = viewModel.getGameRoomByName(roomName).observeAsState()
    var user = "Judge" /*TODO: check if this user is the judge*/
    var clickable = ""
    var meme = "not empty"
    var i = 0

    val sentences = listOf("When you lie down for a quick nap and wake up 8 hours later",
        "When you lie down for a quick nap and wake up 8 hours later",
        "When you lie down for a quick nap and wake up 8 hours later",
        "When you lie down for a quick nap and wake up 8 hours later",
        "When you lie down for a quick nap and wake up 8 hours later") /*TODO: change to sentences chosen by the users (mutable state?)*/

    if(user == "judge"){
        clickable = "yes"
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
                    text = "Score = " + 5 /*TODO: Change to judge's score when score is implemented*/,
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
                            /*TODO: check if gameRoom.value?.players!![player] is not this screen's player*/
                            Text(
                                text = gameRoom.value?.players!![player] /*TODO: instead of id, present the username*/
                                        + " = score" /*TODO: when implemented, show the user score*/,
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
        ){
            if(gameRoom.value?.players?.size != null && gameRoom.value?.players?.size!! > 3){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ){
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ){
                        Card(sentence = sentences[i], memeUrl = clickable, roomName = roomName, navController = navController, true)
                        i++
                    }
                    if(gameRoom.value?.players?.size!! > 4){
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                        ){
                            Card(sentence = sentences[i], memeUrl = clickable, roomName = roomName, navController = navController, true)
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
            ){
                if(gameRoom.value?.players?.size != null && gameRoom.value?.players?.size!! % 2 == 0){
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ){
                        Card(sentence = sentences[i], memeUrl = clickable, roomName = roomName, navController = navController, true)
                        i++
                    }
                }
                MemeCard(memeUrl = meme)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ){
                    Card(sentence = sentences[i], memeUrl = clickable, roomName = roomName, navController = navController, true)
                    i++
                }
                if(gameRoom.value?.players?.size != null && gameRoom.value?.players?.size!! != 4){
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ){
                        Card(sentence = sentences[i], memeUrl = clickable, roomName = roomName, navController = navController, true)
                    }
                }
            }
        }
    }
}