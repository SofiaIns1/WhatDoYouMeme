package pt.isec.sofiaigp.whatdoyoumeme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.DarkBlue
import pt.isec.sofiaigp.whatdoyoumeme.ui.theme.LightBlue
import kotlin.random.Random


@Composable
fun Card(
    sentence: String,
    memeUrl: String,
    roomName: String,
    navController: NavController,
    viewModel: GameViewModel,
    userName: String,
    roomId: String,
    winner: Boolean = false,
    callback: () -> Unit = {}

) {
    val angle = Random.nextInt(-5, 5)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .rotate(angle.toFloat())
            .height(190.dp)
            .width(120.dp)
            .padding(2.dp)
            .background(DarkBlue, RoundedCornerShape(10.dp))
            .padding(1.dp)
            .clickable {
                if (winner) {
                    callback()
                    navController.navigate("Show Winner/${roomName}/$userName/$sentence")

                } else {
                    viewModel.addSelectedCard(sentence, userName, roomId)
                }

            }
    ) {
        Text(
            text = sentence,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 1.em,
            modifier = Modifier.padding(1.dp)
        )
    }

}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun MemeCard(memeUrl: String) {
    if (memeUrl.isBlank()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(190.dp)
                .width(120.dp)
                .padding(2.dp)
                .background(DarkBlue, RoundedCornerShape(10.dp))
                .padding(1.dp)
        ) {
            Text(
                text = "Player",
                color = LightBlue,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Waiting for judge to chose the meme...",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = 1.em,
                modifier = Modifier.padding(1.dp)
            )
        }
    } else {
        val painter = rememberImagePainter(
            data = memeUrl
        )
        Image(
            painter = painter,
            contentDescription = "Bru",
            modifier = Modifier
                .height(190.dp)
                .width(120.dp)
        )
    }
}