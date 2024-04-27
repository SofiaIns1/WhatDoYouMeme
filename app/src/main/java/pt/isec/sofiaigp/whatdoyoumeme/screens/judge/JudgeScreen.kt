package pt.isec.sofiaigp.whatdoyoumeme.screens.judge

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.LightBlue
import kotlin.random.Random

@Composable
fun JudgeScreen(navController: NavController, viewModel: GameViewModel, roomName: String) {
    val gameRoom = viewModel.getGameRoomByName(roomName).observeAsState()
    val coroutineScope = rememberCoroutineScope()
    var imagesURL by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            imagesURL = viewModel.getRandomMemeImages()
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(DarkBlue)
            .padding(20.dp)
            .fillMaxSize()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = "Score = " + 5 /*TODO: Change to judge's score when score is implemented*/,
                    color = Color.White,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Start
                )
            }

            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ){
                LazyColumn(
                    horizontalAlignment = Alignment.End
                ){
                    gameRoom.value?.players?.size?.let {
                        items(it) { player ->
                            /*TODO: check if gameRoom.value?.players!![player] is not the judge*/
                            Text(
                                text = gameRoom.value?.players!![player] /*TODO: instead of id, present the username*/
                                        + " = score" /*TODO: when implemented, show the user score*/,
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
        ){
            for (i in 0 until 2) {
                ImageFromURL(imagesURL.getOrNull(i) ?: "", navController, roomName)
                if(i % 2 == 0){
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
        ){
            ImageFromURL(imagesURL.getOrNull(2) ?: "", navController, roomName)

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ){
                Text(
                    text = "Juri",
                    color = LightBlue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Pick one\nof the\nmemes",
                    color = Color.White,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }

            ImageFromURL(imagesURL.getOrNull(3) ?: "", navController, roomName)
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
        ){
            for (i in 4 until 6) {
                ImageFromURL(imagesURL.getOrNull(i) ?: "", navController, roomName)
                if(i % 2 == 0){
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        ClickableText(
            text = AnnotatedString("Leave party"),
            style = TextStyle(
                color = Color.LightGray,
                textDecoration = TextDecoration.Underline,
                fontSize = 10.sp
            ),
            onClick = { offset ->
                /*TODO delete user from database*/
                /*TODO: select another judge*/
                navController.navigate("Home Screen")
            }
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageFromURL(imageURL: String, navController: NavController, roomName: String) {
    val painter = rememberImagePainter(
        data = imageURL
    )
    val angle = Random.nextInt(-5, 5)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(150.dp)
            .rotate(angle.toFloat())
            .clickable {
                Log.i("Image", imageURL)
                navController.navigate("Judge Wait/${roomName}")
                //navController.navigate("Judge Wait/${imageURL}")
                /*val combinedString = roomName + "," + imageURL
                navController.navigate("Judge Wait/${combinedString}")*/
                /*TODO: send imageURL*/
            }
    )
}