package pt.isec.sofiaigp.whatdoyoumeme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.isec.sofiaigp.whatdoyoumeme.R
import pt.isec.sofiaigp.whatdoyoumeme.components.GoBackBar
import pt.isec.sofiaigp.whatdoyoumeme.components.InputField
import pt.isec.sofiaigp.whatdoyoumeme.components.StepperInputField

@Composable
fun CreateGameScreen(navController: NavHostController) {

    var gameRoomName by remember { mutableStateOf("") }
    var numPlayers by remember { mutableIntStateOf(0) }
    var numRounds by remember { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)
    ) {
        GoBackBar(navController)
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "WDYM logo",
                modifier = Modifier
                    .size(280.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp)
            ) {
                Text(
                    text = "ROOM NAME",
                    fontSize = 27.sp,
                    color = Color(0xFF8175C1),
                    fontWeight = FontWeight.Bold,
                )
                InputField(
                    value = gameRoomName,
                    onValueChange = { gameRoomName = it },
                    placeholder = "RoomNameHere"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "NUMBER OF PLAYERS",
                    fontSize = 23.sp,
                    color = Color(0xFF8175C1),
                    fontWeight = FontWeight.Bold
                )
                StepperInputField(
                    value = numPlayers,
                    onValueChange = { numPlayers = it },
                    placeholder = 0
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "ROUNDS",
                    fontSize = 23.sp,
                    color = Color(0xFF8175C1),
                    fontWeight = FontWeight.Bold
                )
                StepperInputField(
                    value = numRounds,
                    onValueChange = { numRounds = it },
                    placeholder = 0
                )

                Spacer(modifier = Modifier.height(50.dp))

                Button(
                    onClick = {

                    },
                    modifier = Modifier
                        .height(65.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8175C1),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "CREATE",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )

                }


            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CreateGameScreenPreview(){
    CreateGameScreen(rememberNavController())
}