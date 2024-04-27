package pt.isec.sofiaigp.whatdoyoumeme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WaitingRoomScreenTTTTT(viewModel: GameViewModel, roomName: String) {

    val gameRoom = viewModel.getGameRoomByName(roomName).observeAsState()

    val coroutineScope = rememberCoroutineScope()

    var imagesURL by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            imagesURL = viewModel.getRandomMemeImages()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 15.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 0 until 3) {
                    ImageFromURL(imagesURL.getOrNull(i) ?: "")
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 3 until 6) {
                    ImageFromURL(imagesURL.getOrNull(i) ?: "")
                }
            }
        }
    }


//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        gameRoom.value?.roomName?.let {
//            Text(
//                text = it,
//
//                )
//        }
//        gameRoom.value?.players?.size?.toString().let {
//            if (it != null) {
//                Text(
//                    text = it,
//                )
//            }
//        }
//
//
//    }

}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ImageFromURL(imageURL: String) {
    val painter = rememberImagePainter(
        data = imageURL
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(200.dp)
    )
}