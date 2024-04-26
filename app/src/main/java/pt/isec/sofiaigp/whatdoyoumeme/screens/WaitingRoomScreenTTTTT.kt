package pt.isec.sofiaigp.whatdoyoumeme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel

@Composable
fun WaitingRoomScreenTTTTT(viewModel: GameViewModel, roomName: String){

    val gameRoom = viewModel.getGameRoomByName(roomName).observeAsState()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        gameRoom.value?.roomName?.let {
            Text(
                text = it,

                )
        }
        gameRoom.value?.players?.size?.toString().let {
            if (it != null) {
                Text(
                    text = it,
                )
            }
        }


    }

}