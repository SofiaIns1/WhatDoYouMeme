package pt.isec.sofiaigp.whatdoyoumeme.screens

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.isec.sofiaigp.whatdoyoumeme.R
import pt.isec.sofiaigp.whatdoyoumeme.components.InputField
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkLilac
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.Lilac

@Composable
fun HomePageScreen(navController: NavHostController, viewModel: GameViewModel) {
    val context = LocalContext.current

    var userName by remember { mutableStateOf("") }

    fun isValidInput(): Boolean {
        if (userName.isEmpty()) {
            Toast.makeText(context, "Please enter a username", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
        //.padding(bottom = 40.dp)

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
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .padding(bottom = 40.dp)
        ) {
            Text(
                text = "USERNAME",
                fontSize = 30.sp,
                color = Lilac,
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
                    if (isValidInput()) {
                        viewModel.createPlayer(userName, onSuccess = {
                            navController.navigate("Find Game/${userName}")
                        },
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "Username already exists",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        )

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
                    text = "FIND GAME",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )

            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = {
                    if (isValidInput()) {
                        viewModel.createPlayer(userName, onSuccess = {
                            navController.navigate("Create Game/${userName}")

                        },
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "Username already exists",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            })

                    }

                },
                modifier = Modifier
                    .border(BorderStroke(1.dp, Lilac), RoundedCornerShape(30.dp))
                    .height(65.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = DarkLilac
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
                    containerColor = Lilac,
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
        //}

    }

}


@Preview(showBackground = true)
@Composable
fun HomePageScreenPreview() {
    //HomePageScreen(rememberNavController())
}