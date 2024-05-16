package pt.isec.sofiaigp.whatdoyoumeme.data

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GameViewModel() : ViewModel() {
    private val firebaseManager = FirebaseManager()

    private val _gameRooms = MutableLiveData<List<GameRoom>>(emptyList())
    val gameRooms: LiveData<List<GameRoom>> = _gameRooms

    private val _gameRoom = MutableLiveData<GameRoom?>()
    val gameRoom: LiveData<GameRoom?> = _gameRoom

    private val _players = MutableLiveData<List<User>>(emptyList())
    val players: LiveData<List<User>> = _players

    private val _playableCards = MutableLiveData<List<String>>()
    val playableCards: LiveData<List<String>> = _playableCards

    init {
        getGameRoomsList()
    }

    private fun getGameRoomsList() {
        firebaseManager.getGameRoomsList { gameRooms ->
            _gameRooms.postValue(gameRooms)
        }
    }

    fun fetchPlayableCards() {
        viewModelScope.launch {
            try {
                val cards = firebaseManager.getRandomPlayableCards()
                _playableCards.value = cards
            } catch (e: Exception) {
                Log.e("Error", "Error fetching cards", e)
            }
        }
    }

    fun createPlayer(playerName: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        firebaseManager.createPlayer(
            playerName = playerName,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }

    fun createGameRoom(
        roomName: String,
        numPlayers: Int,
        numRounds: Int,
        playerName: String,
        chosenMeme: String? = "",
        selectedCards: List<Map<String, String>>? = null,
        score: List<MutableMap<String, Int>>? = null,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        firebaseManager.createGameRoom(
            roomName,
            numPlayers,
            numRounds,
            playerName,
            chosenMeme,
            selectedCards,
            score,
            onSuccess,
            onFailure
        )


    }

    fun joinGameRoom(playerName: String, roomId: String, maxPlayers: Int) {
        firebaseManager.joinGameRoom(playerName, roomId, maxPlayers)

    }

    fun getGameRoomByName(roomName: String) {
        firebaseManager.getGameRoomByName(roomName) { gameRoom, players ->
            _gameRoom.postValue(gameRoom)
            _players.postValue(players)
        }
    }

    fun startGame(roomId: String, onSuccess: () -> Unit) {
        firebaseManager.startGame(roomId, onSuccess)
    }

    fun hasGameStarted(roomId: String, onUpdate: () -> Unit) {
        firebaseManager.hasGameStarted(roomId) { gameRoom ->
            _gameRoom.postValue(gameRoom)
            onUpdate()
        }
    }

    suspend fun getRandomMemeImages(): List<String> {
        return firebaseManager.getRandomMemeImages()
    }

//    suspend fun getRandomPlayableCards(): List<String> {
//        return firebaseManager.getRandomPlayableCards()
//    }

    fun selectJudge(roomId: String) {
        firebaseManager.selectJudge(roomId) { players ->
            _players.postValue(players)
        }
    }

//    fun isJudge(roomId: String, playerId: String, callback: (Boolean) -> Unit){
//        return firebaseManager.isJudge(roomId, callback)
//    }

    fun isJudge(roomId: String, playerName: String): Boolean {
        for (room in _gameRooms.value!!) {
            if (room.roomId == roomId) {
                for (player in _players.value!!) {
                    if (player.username == playerName) {
                        if (player.role == "judge")
                            return true
                    }
                }
            }
        }

        return false
    }

    fun addChosenMeme(chosenMeme: String, roomId: String) {
        firebaseManager.addChosenMeme(chosenMeme, roomId)
    }

    fun memeUpdated(roomId: String, callback: () -> Unit) {
        firebaseManager.memeUpdated(roomId, callback)
    }

    fun getChosenMeme(roomId: String, onComplete: (String) -> Unit) {
        firebaseManager.getChosenMeme(roomId, onComplete)
    }

    fun addSelectedCard(card: String, playerName: String, roomId: String) {
        firebaseManager.addSelectedCard(card, playerName, roomId)
    }

    fun isCardSelected(playerName: String, roomId: String, callback: () -> Unit) {
        firebaseManager.isCardSelected(playerName, roomId, callback)
    }


    fun allCardsSelected(roomId: String): Boolean {

        for (room in _gameRooms.value!!) {
            if (room.roomId == roomId) {
                if (room.selectedCards?.size == room.currentNumPlayers?.minus(1))
                    return true
            }
        }
        return false
    }

    fun updateScore(chosenCard: String, roomId: String) {
        firebaseManager.updateScore(chosenCard, roomId)
    }

    fun getSelectedCards(roomId: String, onComplete: (List<Map<String, String>>) -> Unit) {
        firebaseManager.getSelectedCards(roomId, onComplete)
    }

    fun getPlayerScore(roomId: String, playerName: String, onComplete: (Int) -> Unit) {
        firebaseManager.getPlayerScore(roomId, playerName, onComplete)
    }

    fun resetMeme(roomId: String){
        firebaseManager.resetMeme(roomId)
    }

    fun resetSelectedCards(roomId: String){
        firebaseManager.resetSelectedCards(roomId)
    }

    fun deletePlayer(roomId: String, playerName: String) {
        firebaseManager.deletePlayer(roomId, playerName)
    }
}