package pt.isec.sofiaigp.whatdoyoumeme.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import pt.isec.sofiaigp.whatdoyoumeme.data.GameViewModel

@Composable
fun JudgeWaitScreen(viewModel : GameViewModel, roomName : String/*, imageUrl : String*/) {
    //val gameRoom = viewModel.getGameRoomByName(roomName).observeAsState()
    Log.i("parametros", roomName)
}