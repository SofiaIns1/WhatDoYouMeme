package pt.isec.sofiaigp.whatdoyoumeme.data

data class GameRoom(
    val roomId: String? = null,
    val roomName: String? = null,
    val players: List<String>? = null, //ids of the players in the room
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