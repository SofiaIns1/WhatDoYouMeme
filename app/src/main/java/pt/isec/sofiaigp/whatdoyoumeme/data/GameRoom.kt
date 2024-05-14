package pt.isec.sofiaigp.whatdoyoumeme.data

data class GameRoom(
    val roomId: String? = null,
    val roomName: String? = null,
    val maxPlayers: Int? = null,
    var currentNumPlayers: Int? = null,
    val numRounds: Int? = null,
    var winner: String? = null,
    var start: Boolean? = false,
    val chosenMeme: String? = null
)


data class User(
    val id: String? = null,
    val username: String? = null,
    val score: Int? = null,
    val role: String? = null,
    val selectedCard: String? = null,
)

object PreviousGameRoomStateManager{
    private val previousGameRoomStates = mutableMapOf<String,GameRoom>()

    fun getPreviousGameRoomState(roomId: String): GameRoom?{
        return previousGameRoomStates[roomId]
    }

    fun updatePreviousGameRoomState(roomId: String, gameRoom: GameRoom) {
        previousGameRoomStates[roomId] = gameRoom
    }
}