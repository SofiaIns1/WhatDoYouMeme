package pt.isec.sofiaigp.whatdoyoumeme.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import kotlinx.coroutines.tasks.await
import kotlin.properties.Delegates

class FirebaseManager() {

    private val db = FirebaseFirestore.getInstance()

    fun createPlayer(
        playerName: String,
        score: Int? = 0,
        role: String? = "player",
        selectedCard: String? = "",
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {

        db.collection("players")
            .whereEqualTo("playerName", playerName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val playerData = hashMapOf(
                        "playerName" to playerName,
                        "score" to score,
                        "role" to role,
                        "selectedCard" to selectedCard,
                    )

                    db.collection("players")
                        .add(playerData)
                        .addOnSuccessListener { documentReference ->
                            Log.d("TAG", "Player added with ID: ${documentReference.id}")
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            Log.d("TAG", "Error adding player", exception)
                        }
                } else {
                    Log.d("TAG", "Player with name $playerName already exists.")
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error checking player existence", exception)
            }
    }


    fun createGameRoom(
        roomName: String,
        maxPlayers: Int,
        numRounds: Int,
        playerName: String,
        chosenMeme: String? = "",
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {

        db.collection("game_rooms")
            .whereEqualTo("roomName", roomName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val gameRoom = hashMapOf(
                        "roomName" to roomName,
                        "maxPlayers" to maxPlayers,
                        "numRounds" to numRounds,
                        "chosenMeme" to chosenMeme
                    )

                    db.collection("game_rooms")
                        .add(gameRoom)
                        .addOnSuccessListener { documentReference ->
                            Log.d("TAG", "Game room created with ID: ${documentReference.id}")
                            joinGameRoom(playerName, documentReference.id, maxPlayers)
                            onSuccess()
                        }
                        .addOnFailureListener { exception ->
                            Log.e("TAG", "Error creating game room", exception)
                        }
                } else {
                    Log.d("TAG", "Game room with name $roomName already exists.")
                    onFailure()
                }
            }

    }


    fun joinGameRoom(
        playerName: String,
        roomId: String,
        maxPlayers: Int
    ) {
        val playersCollection = db.collection("players")

        playersCollection.whereEqualTo("playerName", playerName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    if (querySnapshot.size() < maxPlayers) {
                        for (document in querySnapshot.documents) {
                            val playerId = document.id
                            val roomRef = db.collection("game_rooms").document(roomId)
                                .collection("players").document(playerId)


                            roomRef.set(document.data!!)
                                .addOnSuccessListener {
                                    Log.d(
                                        "TAG",
                                        "Player $playerName added to game room $roomId"
                                    )

                                }
                                .addOnFailureListener { e ->
                                    Log.e("TAG", "Error adding player to game room: $e")
                                }
                        }
                    } else {
                        Log.d("TAG", "Maximum players reached in game room $roomId")
                    }
                } else {
                    Log.d("TAG", "Player $playerName not found")
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error querying players: $e")
            }
    }

    fun getGameRoomsList(onUpdate: (List<GameRoom>) -> Unit) {
        val gameRoomsCollection = db.collection("game_rooms")

        gameRoomsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("TAG", "Error observing game rooms", exception)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val gameRooms = mutableListOf<GameRoom>()

                for (document in snapshot.documents) {
                    val roomId = document.id
                    val roomName = document.getString("roomName")
                    val maxPlayers = document.getLong("maxPlayers")?.toInt()
                    val numRounds = document.getLong("numRounds")?.toInt()
                    val winner = document.getString("winner")
                    val start = document.getBoolean("start")
                    val chosenMeme = document.getString("chosenMeme")

                    getPlayersByGameRoomId(roomId,
                        onSuccess = { players ->
                            val currentNumPlayers = players.size

                            val gameRoom = GameRoom(
                                roomId,
                                roomName,
                                maxPlayers,
                                currentNumPlayers,
                                numRounds,
                                winner,
                                start,
                                chosenMeme
                            )

                            gameRooms.add(gameRoom)

                            if (gameRooms.size == snapshot.documents.size) {
                                onUpdate(gameRooms)
                            }

                        },
                        onFailure = { e ->
                            Log.e(
                                "TAG", "Error getting players for room $roomName",
                                e as Throwable?
                            )
                        }
                    )


                }

                if (gameRooms.isEmpty()) {
                    onUpdate(emptyList())
                    return@addSnapshotListener
                }

            }
        }
    }

    fun getGameRoomByName(roomName: String, onUpdate: (GameRoom, List<User>) -> Unit) {

        db.collection("game_rooms")
            .whereEqualTo("roomName", roomName)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        var roomId = document.id
                        val maxPlayers = document.getLong("maxPlayers")?.toInt()
                        val numRounds = document.getLong("numRounds")?.toInt()
                        val winner = document.getString("winner")
                        val start = document.getBoolean("start")
                        val chosenMeme = document.getString("chosenMeme")

                        getPlayersByGameRoomId(
                            roomId,
                            onSuccess = { players ->
                                val gameRoom =
                                    GameRoom(
                                        roomId,
                                        roomName,
                                        maxPlayers,
                                        players.size,
                                        numRounds,
                                        winner,
                                        start,
                                        chosenMeme
                                    )

                                val previousGameRoom =
                                    PreviousGameRoomStateManager.getPreviousGameRoomState(roomId)
                                if (gameRoom != previousGameRoom) {
                                    saveGameRoomToFirestore(gameRoom)
                                    updatePlayersInFirestore(roomId, players)
                                    onUpdate(gameRoom, players)
                                }

                                PreviousGameRoomStateManager.updatePreviousGameRoomState(
                                    roomId,
                                    gameRoom
                                )


                            },
                            onFailure = {
                                Log.e("TAG", "Error getting players for game room $roomName")
                            }
                        )

                    }


                } else {
                    Log.e("TAG", "Error getting game room")
                }
            }
    }

    private fun getPlayersByGameRoomId(
        roomId: String,
        onSuccess: (List<User>) -> Unit,
        onFailure: (Any?) -> Unit
    ) {
        val playersRef = db.collection("game_rooms").document(roomId).collection("players")
        playersRef.addSnapshotListener { playerSnapshot, e ->

            if (e != null) {
                Log.w("TAG", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (playerSnapshot != null) {
                val players = mutableListOf<User>()
                for (playerDocument in playerSnapshot.documents) {
                    val playerId = playerDocument.id
                    val username = playerDocument.getString("playerName")
                    val score = playerDocument.getLong("score")?.toInt()
                    val role = playerDocument.getString("role")
                    val selectedCard = playerDocument.getString("selectedCard")
                    val user = User(playerId, username, score, role, selectedCard)
                    players.add(user)
                }
                onSuccess(players)
            }

        }

    }


    private fun saveGameRoomToFirestore(gameRoom: GameRoom) {
        gameRoom.roomId?.let {
            db.collection("game_rooms")
                .document(it)
                .update(
                    mapOf(
                        "maxPlayers" to gameRoom.maxPlayers,
                        "currentNumPlayers" to gameRoom.currentNumPlayers,
                        "numRounds" to gameRoom.numRounds,
                        "winner" to gameRoom.winner,
                        "start" to gameRoom.start,
                        "chosenMeme" to gameRoom.chosenMeme
                    )
                )
                .addOnSuccessListener {
                    Log.d("TAG", "Game Room updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("TAG", "Error updating game room", e)
                }
        }
    }

    private fun updatePlayersInFirestore(roomId: String, players: List<User>) {
        val playersRef = db.collection("game_rooms").document(roomId).collection("players")
        for (player in players) {
            player.id?.let {
                playersRef.document(it)
                    .update(
                        mapOf(
                            "score" to player.score,
                            "role" to player.role,
                            "selectedCard" to player.selectedCard
                        )
                    )
                    .addOnSuccessListener {
                        Log.d("TAG", "Player ${player.username} updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("TAG", "Error updating player ${player.username}", e)
                    }
            }
        }
    }

    fun hasGameStarted(roomId: String, onUpdate: (GameRoom) -> Unit) {
        db.collection("game_rooms").document(roomId)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                if (e != null) {
                    Log.e("TAG", "Listen failed", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val isStart = snapshot.getBoolean("start") ?: false
                    if (isStart == true) {
                        val roomName = snapshot.getString("roomName")
                        val maxPlayers = snapshot.getLong("maxPlayers")?.toInt()
                        val numRounds = snapshot.getLong("numRounds")?.toInt()
                        val currentNumPlayers = snapshot.getLong("currentNumPlayers")?.toInt()
                        val winner = snapshot.getString("winner")
                        val start = snapshot.getBoolean("start")
                        val chosenMeme = snapshot.getString("chosenMeme")
                        onUpdate(
                            GameRoom(
                                roomId,
                                roomName,
                                maxPlayers,
                                currentNumPlayers,
                                numRounds,
                                winner,
                                start,
                                chosenMeme
                            )
                        )
                    }

                }
            }
    }


    fun startGame(roomId: String, onSuccess: () -> Unit) {
        db.collection("game_rooms").document(roomId)
            .get()
            .addOnSuccessListener { snapshot ->
                val isStarted = snapshot.getBoolean("start") ?: false
                if (!isStarted) {
                    db.collection("game_rooms").document(roomId)
                        .update("start", true)
                        .addOnSuccessListener {
                            Log.d("TAG", "State updated successfully for game room $roomId")

                        }.addOnFailureListener {
                            Log.e("TAG", "Failed to updating game room's state")
                        }

                } else {
                    Log.d("TAG", "Game for game room $roomId is already started")
                }


            }

    }

    suspend fun getRandomMemeImages(): List<String> {
        val imagesURL = mutableListOf<String>()

        try {
            val querySnapshot = db.collection("memes")
                .limit(6)
                .get()
                .await()

            if (querySnapshot.size() < 6) {
                throw Exception("Not enough images on firestore")
            }

            val shuffleDocuments = querySnapshot.documents.shuffled()

            for (i in 0 until 6) {
                val imageURL = shuffleDocuments[i].getString("imageURL")
                imageURL?.let {
                    imagesURL.add(it)
                    Log.d("TAG", "IMAGES: $imageURL\n")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return imagesURL

    }

    suspend fun getRandomPlayableCards(): List<String> {
        val sentences = mutableListOf<String>()
        try {
            val querySnapshot = db.collection("playable_cards").get().await()

            if (querySnapshot.size() < 7) {
                throw Exception("Not enough playable cards on firestore")
            }

            val shuffleDocuments = querySnapshot.documents.shuffled()

            for (i in 0 until 7) {
                val sentence = shuffleDocuments[i].getString("text")
                sentence?.let {
                    sentences.add(it)
                    Log.d("TAG", "SENTENCES: $sentence")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sentences
    }

    fun selectJudge(roomId: String, onUpdate: (List<User>) -> Unit) {
        val roomRef = db.collection("game_rooms").document(roomId)

        roomRef.collection("players")
            .get()
            .addOnSuccessListener { playerSnapshot ->
                val players = mutableListOf<String>()
                for (playerDocument in playerSnapshot.documents) {
                    val playerId = playerDocument.id
                    players.add(playerId)
                }

                if (players.isNotEmpty()) {
                    val randomIndex = (0 until players.size).random()
                    val judgePlayerId = players[randomIndex]

                    roomRef.collection("players").document(judgePlayerId)
                        .update("role", "judge")
                        .addOnSuccessListener {
                            Log.d("TAG", "Player $judgePlayerId selected as judge of the game")

                            getPlayersByGameRoomId(roomId, onSuccess = { players ->
                                updatePlayersInFirestore(roomId, players)
                                onUpdate(players)

                            },
                                onFailure = {})

                        }
                        .addOnFailureListener { e ->
                            Log.e("TAG", "Error updating the judge role", e)
                        }
                } else {
                    Log.d("TAG", "No players in the room $roomId")
                }
            }.addOnFailureListener { e ->
                Log.e("TAG", "Error getting players for game room $roomId", e)
            }
    }


//    fun isJudge(roomId: String, callback: (Boolean) -> Unit) {
//        val roomRef = db.collection("game_rooms").document(roomId)
//
//        roomRef.collection("players")
//            .addSnapshotListener { snapshot, e ->
//                if (e != null) {
//                    Log.w("TAG", "Listen failed", e)
//                    callback(false)
//                    return@addSnapshotListener
//                }
//
//                var isJudge = false
//                if (snapshot != null) {
//                    for (document in snapshot.documents) {
//                        val role = document.getString("role")
//                        if (role == "judge") {
//                            isJudge = true
//                            break
//                        }
//                    }
//                }
//                callback(isJudge)
//            }
//
//    }

    fun addSelectedCard(card: String, playerName: String, roomId: String) {
        val roomRef = db.collection("game_rooms").document(roomId)

        roomRef.collection("players")
            .whereEqualTo("playerName", playerName)
            .get()
            .addOnSuccessListener { playerSnapshot ->
                for (playerDocument in playerSnapshot.documents) {
                    val playerId = playerDocument.id
                    val playerRef = roomRef.collection("players").document(playerId)

                    playerRef.update("selectedCard", card)
                        .addOnSuccessListener {
                            Log.d("TAG", "Selected card updated for player $playerName")
                        }
                        .addOnFailureListener { e ->
                            Log.e("TAG", "Error updating card for player $playerName", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error fetching player document for $playerName", e)
            }

    }

    fun isCardSelected(playerName: String, roomId: String, callback: (Boolean) -> Unit) {
        db.collection("game_rooms").document(roomId)
            .collection("players")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    for (playerDocument in snapshot.documents) {
                        val name = playerDocument.getString("playerName")
                        if (name == playerName) {
                            val selectedCard = playerDocument.getString("selectedCard")
                            if(selectedCard != "")
                                callback(true)
                            break
                        }
                    }
                }
            }
    }


    fun getSelectedCards(roomId: String, onComplete: (List<String>) -> Unit) {

        val roomRef = db.collection("game_rooms").document(roomId)

        roomRef.collection("players")
            .addSnapshotListener { playerSnapshot, e ->
                if (e != null) {
                    Log.w("TAG", "listen failed", e)
                    return@addSnapshotListener
                }

                if (playerSnapshot != null) {
                    val selectedCards = mutableListOf<String>()
                    for (playerDocument in playerSnapshot.documents) {
                        val selectedCard = playerDocument.getString("selectedCard")
                        if (!selectedCard.isNullOrEmpty()) {
                            selectedCards.add(selectedCard)
                        }
                    }
                    onComplete(selectedCards)
                }


//        val roomRef = db.collection("game_rooms").document(roomId)
//
//        roomRef.collection("players")
//            .get()
//            .addOnSuccessListener { playerSnapshot ->
//                val selectedCards = mutableListOf<String>()
//                for (playerDocument in playerSnapshot.documents) {
//                    val selectedCard = playerDocument.getString("selectedCard")
//                    if (!selectedCard.isNullOrEmpty()) {
//                        selectedCards.add(selectedCard)
//                    }
//                }
//                onComplete(selectedCards)
//
//            }.addOnFailureListener { e ->
//                Log.e("TAG", "Error getting selected cards", e)
//                onComplete(emptyList())
//            }
            }
    }

//    fun allCardsSelected(roomId: String, callback: () -> Unit){
//
//        db.collection("game_rooms").document(roomId)
//            .collection("players")
//            .addSnapshotListener{snapshot, e ->
//                if(e != null){
//                    Log.w("TAG", "listen failed", e)
//                    return@addSnapshotListener
//                }
//
//                if(snapshot != null){
//                    for(player in snapshot.documents){
//                        if (player.getString("role") != "judge"){
//                            if(player.getString("selectedCard") != ""){
//                                callback()
//                            }
//                        }
//                    }
//                }
//            }
//    }

    fun addChosenMeme(chosenMeme: String, roomId: String) {
        db.collection("game_rooms").document(roomId)
            .update("chosenMeme", chosenMeme)
            .addOnSuccessListener {
                Log.d("TAG", "Chosen meme updated for room $roomId")

            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error updating chosen meme for room $roomId", e)
            }
    }

    fun memeUpdated(roomId: String, callback: () -> Unit) {
        db.collection("game_rooms")
            .document(roomId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("TAG", "listen failed", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val chosenMeme = snapshot.getString("chosenMeme")
                    if (chosenMeme != "") {
                        callback()
                    }
                }
            }
    }

    fun getChosenMeme(roomId: String, onComplete: (String) -> Unit) {
        db.collection("game_rooms").document(roomId)
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val chosenMeme = snapshot.getString("chosenMeme")
                    if (chosenMeme != null) {
                        onComplete(chosenMeme)
                        return@addSnapshotListener
                    }
                }
                //onComplete("")
            }
    }

    fun updateScore(chosenCard: String, roomId: String) {
        val roomRef = db.collection("game_rooms").document(roomId)

        roomRef.collection("players")
            .get()
            .addOnSuccessListener { playerSnapshot ->
                for (playerDocument in playerSnapshot.documents) {
                    val playerId = playerDocument.id
                    val playerRef = roomRef.collection("players").document(playerId)
                    val selectedCard = playerDocument.getString("selectedCard")


                    if (selectedCard == chosenCard) {
                        playerRef.update("score", FieldValue.increment(1))
                            .addOnSuccessListener {
                                Log.d("TAG", "Score updated for player $playerId")
                            }
                            .addOnFailureListener { e ->
                                Log.e("TAG", "Error updating score for player $playerId", e)
                            }
                    }

                    playerRef.update("selectedCard", "")
                        .addOnSuccessListener {
                            Log.d("TAG", "Selected card reset")
                        }
                        .addOnFailureListener { e ->
                            Log.e("TAG", "Error reseting selected card")

                        }

                }

            }.addOnFailureListener { e ->
                Log.e("TAG", "Error comparing slected card with judge's card", e)
            }
    }


    fun getPlayerScore(roomId: String, playerName: String, onComplete: (Int) -> Unit) {
        val roomRef = db.collection("game_rooms").document(roomId)

        val players = roomRef.collection("players")

        players
            .whereEqualTo("playerName", playerName)
            .addSnapshotListener { playerSnapshot, e ->
                if (playerSnapshot != null) {
                    for (playerDocument in playerSnapshot.documents) {
                        val score = playerDocument.getLong("score")?.toInt() ?: 0
                        onComplete(score)
                        return@addSnapshotListener
                    }
                }
                // onComplete(0)
            }

    }

    fun deletePlayer(roomId: String, playerName: String) {
        val roomRef = db.collection("game_rooms").document(roomId)

        val players = roomRef.collection("players")

        players
            .whereEqualTo("playerName", playerName)
            .get()
            .addOnSuccessListener { playerSnapshot ->
                for (playerDocument in playerSnapshot.documents) {
                    val playerId = playerDocument.id
                    val playerRef = players.document(playerId)

                    playerRef.delete()
                        .addOnSuccessListener {
                            Log.d("TAG", "Player $playerName deleted")
                        }
                        .addOnFailureListener { e ->
                            Log.e("TAG", "Error deleting player $playerName", e)
                        }

                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error getting players", e)
            }
    }
}
