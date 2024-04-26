package pt.isec.sofiaigp.whatdoyoumeme.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel() : ViewModel() {
    private val firebaseManager = FirebaseManager()

    private val _gameRooms = mutableStateOf<List<GameRoom>>(emptyList())
    val gameRooms: State<List<GameRoom>> = _gameRooms

    init {
        getGameRoomsList()
    }

    private fun getGameRoomsList() {
        firebaseManager.getGameRoomsList { gameRooms ->
            _gameRooms.value = gameRooms
        }
    }

    fun createPlayer(playerName: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firebaseManager.createPlayer(playerName = playerName, onSuccess = onSuccess, onFailure = onFailure)


    }

    fun createGameRoom(roomName: String, numPlayers: Int, numRounds: Int, playerName: String) {
        firebaseManager.createGameRoom(roomName, numPlayers, numRounds, playerName)
        /*TODO: only allow to create room if a name has been given*/
    }

    fun joinGameRoom(playerName: String, roomId: String, maxPlayers: Int) {
        firebaseManager.joinGameRoom(playerName, roomId, maxPlayers)
    }

    fun getGameRoomByName(roomName: String): MutableLiveData<GameRoom?> {
        return firebaseManager.getGameRoomByName(roomName)
    }

}