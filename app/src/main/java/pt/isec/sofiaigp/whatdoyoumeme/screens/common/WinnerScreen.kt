package pt.isec.sofiaigp.whatdoyoumeme.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.sofiaigp.whatdoyoumeme.R
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.LightBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.Lilac

@Composable
fun WinnerScreen(navController: NavHostController, viewModel: GameViewModel, roomName: String) {

    val gameRoom = viewModel.getGameRoomByName(roomName).observeAsState()

    gameRoom.value?.winner = "Maria" /*TODO: When winner implemented, remove*/

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(DarkBlue)
            .fillMaxSize()
            .padding(10.dp, 40.dp, 10.dp, 40.dp)
    ){
        Text(
            text = "WHAT DO YOU",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "MEME?",
            color = Color.White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "WINNER",
            color = Color.White,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold
        )

        gameRoom.value?.winner?.let {
            Text(
                text = it,
                color = LightBlue,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
        ){
            LazyColumn (
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                gameRoom.value?.players?.size?.let {
                    items(it){ player ->
                        if(gameRoom.value?.players!![player] != gameRoom.value?.winner){
                            Text(
                                text = gameRoom.value?.players!![player] + " = score", /*TODO get player name instead of id and score*/
                                color = Color.LightGray,
                                fontSize = 33.sp,
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                navController.navigate("Home Screen")
                /*TODO delete user from DB; when all users have been deleted, delete game Room*/
            },
            modifier = Modifier
                .height(65.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Lilac,
                contentColor = Color.White
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.home),
                contentDescription = "Home",
                tint = Color.White
            )
        }
    }
}