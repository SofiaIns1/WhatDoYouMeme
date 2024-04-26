package pt.isec.sofiaigp.whatdoyoumeme.screens

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.sofiaigp.whatdoyoumeme.data.GameRoom
import pt.isec.sofiaigp.whatdoyoumeme.components.GoBackBarWhite
import pt.isec.sofiaigp.whatdoyoumeme.components.SearchInputField
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkLilac
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.Lilac
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.PurpleGrey80


@Composable
fun GameRoomList(
    gameRooms: List<GameRoom>, selectedGameRoomId: MutableState<String>, maxPlayers: MutableState<Int>, onGameRoomClicked: (String, Int, String) -> Unit
) {
    LazyColumn {
        itemsIndexed(gameRooms) { _, gameRoom ->
            Row(
                modifier = Modifier
                    .background(if(selectedGameRoomId.value == gameRoom.roomId) PurpleGrey80 else Color.Transparent)
            ) {
                gameRoom.roomName?.let {
                    Text(
                        text = it,
                        color = DarkLilac,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(2f)
                            .padding(5.dp)
                            .clickable {
                                selectedGameRoomId.value = gameRoom.roomId.toString()
                                maxPlayers.value = gameRoom.maxPlayers!!
                                gameRoom.roomId?.let { it1 ->
                                    onGameRoomClicked(
                                        it1,
                                        gameRoom.maxPlayers,
                                        it
                                    )
                                }


                            }
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = DarkLilac
                )

                Text(
                    text = gameRoom.currentNumPlayers.toString() + "/" + gameRoom.maxPlayers.toString(),
                    color = DarkLilac,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp)
                )

            }
            Divider(
                color = DarkLilac
            )
        }
    }
}


@Composable
fun FindGameScreen(navController: NavHostController, viewModel: GameViewModel, userName: String) {
    var roomSearch by remember {
        mutableStateOf("")
    }

    val selectedGameRoomId = remember {
        mutableStateOf<String>("")
    }
    val maxPlayers = remember {
        mutableIntStateOf(0)
    }

    var roomName: String = ""

    val gameRooms = viewModel.gameRooms.value

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(DarkBlue)
            .padding(bottom = 20.dp)
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(DarkBlue)
                .padding(bottom = 20.dp)
                .weight(1f)
        ){
            GoBackBarWhite(navController)

            Text(
                text = "WHAT DO YOU",
                color = Color.White,
                fontSize = 33.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "MEME?",
                color = Color.White,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                SearchInputField(
                    value = roomSearch,
                    onValueChange = {roomSearch = it},
                    placeholder = "Search for Game Room"
                )

                Spacer(modifier = Modifier.padding(vertical = 5.dp))

                Column(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(Color.White)
                        .fillMaxSize(),
                ) {
                    Row (
                        modifier = Modifier
                    ){
                        Text(
                            text = "ROOM NAME",
                            color = DarkLilac,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(2f)
                                .padding(5.dp)
                        )

                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = DarkLilac
                        )

                        Text(
                            text = "PLAYERS",
                            color = DarkLilac,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp)
                        )

                    }
                    Divider(
                        color = DarkLilac
                    )

                    GameRoomList(
                        gameRooms = gameRooms,
                        selectedGameRoomId,
                        maxPlayers,
                        onGameRoomClicked = { roomId, max, name ->
                            selectedGameRoomId.value = roomId
                            maxPlayers.intValue = max
                            roomName = name
                        }
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 5.dp))
            }

        }
        Column (
            modifier = Modifier
                .padding(20.dp, 0.dp, 20.dp, 20.dp)
        ){
            Button(
                onClick = {
                    selectedGameRoomId.value.let { roomId ->
                        viewModel.joinGameRoom(userName, roomId, maxPlayers.intValue)
                        navController.navigate("Waiting Room/${roomName}")
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
                    text = "START GAME",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )

            }
        }
    }
}


//@Composable
//fun FindGameScreen(navController: NavHostController) {
//    var roomSearch by remember {
//        mutableStateOf("")
//    }
//
//    val gameRooms = listOf(GameRoom("Room 1", 1, 2), GameRoom("Room 2", 2, 3), GameRoom("Room 3", 3, 4)) /*TODO buscar à DB*/
//
//    Column (
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier
//            .background(DarkBlue)
//            .padding(bottom = 20.dp)
//            .fillMaxSize()
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier
//                .background(DarkBlue)
//                .padding(bottom = 20.dp)
//                .weight(1f)
//        ){
//            GoBackBarWhite(navController)
//
//            Text(
//                text = "WHAT DO YOU",
//                color = Color.White,
//                fontSize = 33.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Text(
//                text = "MEME?",
//                color = Color.White,
//                fontSize = 64.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Column(
//                modifier = Modifier
//                    .padding(20.dp)
//            ) {
//                SearchInputField(
//                    value = roomSearch,
//                    onValueChange = {roomSearch = it},
//                    placeholder = "Search for Game Room"
//                )
//
//                Spacer(modifier = Modifier.padding(vertical = 5.dp))
//
//                Column(
//                    modifier = Modifier
//                        .clip(shape = RoundedCornerShape(30.dp))
//                        .background(Color.White)
//                        .fillMaxSize(),
//                ) {
//                    Row (
//                        modifier = Modifier
//                    ){
//                        Text(
//                            text = "ROOM NAME",
//                            color = DarkLilac,
//                            fontSize = 20.sp,
//                            textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier
//                                .weight(2f)
//                                .padding(5.dp)
//                        )
//
//                        Divider(
//                            modifier = Modifier
//                                .height(40.dp)
//                                .width(1.dp),
//                            color = DarkLilac
//                        )
//
//                        Text(
//                            text = "PLAYERS",
//                            color = DarkLilac,
//                            fontSize = 20.sp,
//                            textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier
//                                .weight(1f)
//                                .padding(5.dp)
//                        )
//
//                    }
//                    Divider(
//                        color = DarkLilac
//                    )
//
//                    LazyColumn (
//                        modifier = Modifier
//                            .padding(0.dp)
//                    ){
//                        items(gameRooms){i ->
//                            Row (
//                                modifier = Modifier
//                            ){
//                                i.name?.let {
//                                    Text(
//                                        text = it,
//                                        color = DarkLilac,
//                                        fontSize = 20.sp,
//                                        textAlign = TextAlign.Center,
//                                        modifier = Modifier
//                                            .weight(2f)
//                                            .padding(5.dp)
//                                    )
//                                }
//
//                                Divider(
//                                    modifier = Modifier
//                                        .height(40.dp)
//                                        .width(1.dp),
//                                    color = DarkLilac
//                                )
//
//                                Text(
//                                    text = i.players.toString() + "/" + i.maxPlayers.toString(),
//                                    color = DarkLilac,
//                                    fontSize = 20.sp,
//                                    textAlign = TextAlign.Center,
//                                    modifier = Modifier
//                                        .weight(1f)
//                                        .padding(5.dp)
//                                )
//
//                            }
//                            Divider(
//                                color = DarkLilac
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.padding(vertical = 5.dp))
//            }
//
//        }
//        Column (
//            modifier = Modifier
//                .padding(20.dp, 0.dp, 20.dp, 20.dp)
//        ){
//            Button(
//                onClick = {
//                    /*TODO*/
//                },
//                modifier = Modifier
//                    .height(65.dp)
//                    .fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Lilac,
//                    contentColor = Color.White
//                )
//            ) {
//                Text(
//                    text = "START GAME",
//                    fontSize = 30.sp,
//                    fontWeight = FontWeight.Bold,
//                )
//
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
private fun FindGameScreenPreview() {
    // FindGameScreen(rememberNavController())
}