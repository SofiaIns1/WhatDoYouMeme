package pt.isec.sofiaigp.whatdoyoumeme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import pt.isec.sofiaigp.whatdoyoumeme.components.InputField

@Composable
fun HomePageScreen(navController: NavHostController) {

    var userName by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)

    ) {
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
                    text = "USERNAME",
                    fontSize = 30.sp,
                    color = Color(0xFF8175C1),
                    fontWeight = FontWeight.Bold,
                )
                InputField(
                    value = userName,
                    onValueChange = { userName = it },
                    placeholder = "YourUsernameHere"
                )
                Spacer(modifier = Modifier.padding(20.dp))

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
                        text = "FIND GAME",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                    )

                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {
                        navController.navigate("Create Game")
                    },
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color(0xFF8175C1)), RoundedCornerShape(30.dp))
                        .height(65.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF53479E)
                    )

                ) {
                    Text(
                        text = "CREATE GAME",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                }
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {
                        navController.navigate("Game Rules")
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
                        text = "GAME RULES",
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
fun HomePageScreenPreview() {
    HomePageScreen(rememberNavController())
}