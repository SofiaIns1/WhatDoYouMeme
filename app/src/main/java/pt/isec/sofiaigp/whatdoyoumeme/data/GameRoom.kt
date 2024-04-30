package pt.isec.sofiaigp.whatdoyoumeme.data

data class GameRoom(
    val roomId: String? = null,
    val roomName: String? = null,
    var players : List<User>? = null,
    val maxPlayers: Int? = null,
    var currentNumPlayers: Int? = null,
    val numRounds: Int? = null,
    var winner: String? = null
    //val currentGameState: GameState
)


data class GameState(
    val score: Int,
    //val gamaStatus: GameStatus ?? tipo enum: started, waiting, and wtv
    val remainingTime: Long,
    val currentRound: Int
)

data class User(
    val id: String? = null,
    val username: String? = null,
    val score: Int? = null,
    val role: String? = null,
    val selectedCard: String?= null
)