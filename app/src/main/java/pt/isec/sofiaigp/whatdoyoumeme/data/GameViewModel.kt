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

    fun createGameRoom(roomName: String, numPlayers: Int, numRounds: Int, playerName: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firebaseManager.createGameRoom(roomName, numPlayers, numRounds, playerName, onSuccess, onFailure)
    }

    fun joinGameRoom(playerName: String, roomId: String, maxPlayers: Int) {
        firebaseManager.joinGameRoom(playerName, roomId, maxPlayers)
    }

    fun getGameRoomByName(roomName: String): MutableLiveData<GameRoom?> {
        return firebaseManager.getGameRoomByName(roomName)
    }

    suspend fun getRandomMemeImages(): List<String>{
        return firebaseManager.getRandomMemeImages()
    }

    suspend fun getRandomPlayableCards(): List<String>{
        return firebaseManager.getRandomPlayableCards()
    }

    fun selectJudge(roomId: String){
        firebaseManager.selectJudge(roomId)
    }

    fun isJudge(roomId: String): Boolean{
        return firebaseManager.isJudge(roomId)
    }

    fun addSelectedCard(card: String, playerName: String, roomId: String){
        firebaseManager.addSelectedCard(card, playerName, roomId)
    }

    fun updateScore(chosenCard: String, roomId: String){
        firebaseManager.updateScore(chosenCard, roomId)
    }

    fun getSelectedCards(roomId: String, onComplete: (List<String>) -> Unit){
        firebaseManager.getSelectedCards(roomId, onComplete)
    }

    fun getPlayerScore(roomId: String, playerName: String, onComplete: (Int) -> Unit){
        firebaseManager.getPlayerScore(roomId, playerName, onComplete)
    }

    fun deletePlayer(roomId: String, playerName: String){
        firebaseManager.deletePlayer(roomId, playerName)
    }
}