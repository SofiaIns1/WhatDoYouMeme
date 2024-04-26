package pt.isec.sofiaigp.whatdoyoumeme.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.sofiaigp.whatdoyoumeme.R
import pt.isec.sofiaigp.whatdoyoumeme.components.GoBackBarWhite
import pt.isec.sofiaigp.whatdoyoumeme.data.GameRoom
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.Lilac

@Composable
fun WaitingRoomScreen(navController: NavHostController, roomName: String, viewModel: GameViewModel) {
    Log.i("Waiting Room", roomName)
    val gameRoom = GameRoom(roomName = "test", players = listOf("Sofia", "Maria", "Dani"), maxPlayers = 3, currentNumPlayers = 3)
    //val gameRoom = viewModel.getRoomById(roomId)/*TODO*/
    val aux = if(gameRoom.maxPlayers!! <= 4){
        2
    } else{
        3
    }

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
        ) {
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

            Spacer(modifier = Modifier.height(10.dp))
            
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "ROOM: " + gameRoom.roomName,
                    color = Lilac,
                    fontSize = 24.sp
                )
                Text(
                    text = "PLAYERS: " + gameRoom.currentNumPlayers.toString() + "/" + gameRoom.maxPlayers,
                    color = Lilac,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Row{
                    for(i in 0..<aux){
                        if(i < gameRoom.currentNumPlayers!!){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ){
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.user),
                                    contentDescription = "Player",
                                    tint = Lilac
                                )
                                gameRoom.players?.get(i)?.let {
                                    Text(
                                        text = it, /*TODO search player DB to get the user name instead of its id*/
                                        fontSize = 20.sp,
                                        color = Lilac
                                    )
                                }
                            }
                        }
                        else{
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.waiting),
                                contentDescription = "Player",
                                tint = Color.White
                            )
                        }
                        if(i < aux - 1){
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row{
                    for(i in aux..<gameRoom.maxPlayers){
                        if(i < gameRoom.currentNumPlayers!!){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ){
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.user),
                                    contentDescription = "Player",
                                    tint = Lilac
                                )
                                gameRoom.players?.get(i)?.let {
                                    Text(
                                        text = it,/*TODO search player DB to get the user name instead of its id*/
                                        fontSize = 20.sp,
                                        color = Lilac
                                    )
                                }
                            }

                        }
                        else{
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.waiting),
                                contentDescription = "Player",
                                tint = Color.White
                            )
                        }
                        if(i < gameRoom.maxPlayers - 1){
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
                
                if(gameRoom.currentNumPlayers == gameRoom.maxPlayers){
                    Spacer(modifier = Modifier.height(25.dp))

                    Button(
                        onClick = {
                            /*TODO Go to player screen*/
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
    }
}