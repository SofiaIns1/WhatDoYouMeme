package pt.isec.sofiaigp.whatdoyoumeme.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

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
                        "selectedCard" to selectedCard
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
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {

        db.collection("game_rooms")
            .whereEqualTo("roomName", roomName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val gameRoom = hashMapOf(
                        "roomName" to roomName,
                        "players" to emptyList<User>(),
                        "maxPlayers" to maxPlayers,
                        "numRounds" to numRounds
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

    fun joinGameRoom(playerName: String, roomId: String, maxPlayers: Int) {
        val playersCollection = db.collection("players")

        playersCollection.whereEqualTo("playerName", playerName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val playerId = document.id
                        val roomRef =
                            db.collection("game_rooms").document(roomId).collection("players")

                        val p = querySnapshot.documents
                        if (p.size < maxPlayers) {
                            roomRef.document(playerId).set(document.data!!)
                                .addOnSuccessListener {
                                    Log.d(
                                        "TAG",
                                        "Player $playerName added to game room $roomId"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.e("TAG", "Error adding player to game room: $e")
                                }
                        } else {
                            Log.d("TAG", "Maximum players reached in game room $roomId")

                        }
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
                val userMap = mutableMapOf<String, List<User>>()

                for (document in snapshot.documents) {
                    val roomId = document.id
                    val roomName = document.getString("roomName")
                    val maxPlayers = document.getLong("maxPlayers")?.toInt()
                    val numRounds = document.getLong("numRounds")?.toInt()

                    val gameRoom = GameRoom(
                        roomId,
                        roomName,
                        emptyList(),
                        maxPlayers,
                        0,
                        numRounds
                    )
                    gameRooms.add(gameRoom)

                    val playersRef = document.reference.collection("players")
                    playersRef.get().addOnSuccessListener { playerSnapshot ->
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
                        userMap[roomId] = players

                        if (userMap.size == snapshot.documents.size) {
                            for (room in gameRooms) {
                                room.players = userMap[room.roomId]
                                room.currentNumPlayers = room.players?.size ?: 0
                            }
                            onUpdate(gameRooms)
                        }
                    }.addOnFailureListener { e ->
                        Log.e("TAG", "Error getting players for room $roomId", e)
                    }
                }
            }
        }
    }


    fun getGameRoomByName(roomName: String): MutableLiveData<GameRoom?> {
        val gameRoomLiveData = MutableLiveData<GameRoom?>()
        db.collection("game_rooms")
            .whereEqualTo("roomName", roomName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val roomId = document.id
                        val maxPlayers = document.getLong("maxPlayers")?.toInt()
                        val numRounds = document.getLong("numRounds")?.toInt()

                        val gameRoom = GameRoom(
                            roomId,
                            roomName,
                            emptyList(),
                            maxPlayers,
                            0,
                            numRounds
                        )

                        val playersRef = document.reference.collection("players")
                        playersRef.get().addOnSuccessListener { playerSnapshot ->
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
                            gameRoom.players = players
                            gameRoom.currentNumPlayers = players.size

                            gameRoomLiveData.value = gameRoom
                        }.addOnFailureListener { e ->
                            Log.e("TAG", "Error getting players for room $roomName", e)
                        }
                    }
                } else {
                    Log.d("TAG", "Game Room not found")
                    gameRoomLiveData.value = null
                }
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error getting game room: $e")
                gameRoomLiveData.value = null
            }
        return gameRoomLiveData
    }


    suspend fun getRandomMemeImages(): List<String> {
        val imagesURL = mutableListOf<String>()

        try {
            val querySnapshot = db.collection("memes").get().await()

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

    fun selectJudge(roomId: String) {
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

    fun isJudge(roomId: String): Boolean {
        var isJudge = false
        val roomRef = db.collection("game_rooms").document(roomId)

        roomRef.collection("players")
            .get()
            .addOnSuccessListener { playerSnapshot ->
                for (playerDocument in playerSnapshot.documents) {
                    val role = playerDocument.getString("role")
                    if (role == "judge") {
                        isJudge = true
                        break
                    }
                }
            }.addOnFailureListener { e ->
                Log.e("TAG", "Error getting players from room $roomId", e)
            }
        return isJudge
    }

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

    fun getSelectedCards(roomId: String, onComplete: (List<String>) -> Unit) {

        val roomRef = db.collection("game_rooms").document(roomId)

        roomRef.collection("players")
            .get()
            .addOnSuccessListener { playerSnapshot ->
                val selectedCards = mutableListOf<String>()
                for (playerDocument in playerSnapshot.documents) {
                    val selectedCard = playerDocument.getString("selectedCard")
                    if (!selectedCard.isNullOrEmpty()) {
                        selectedCards.add(selectedCard)
                    }
                }
                onComplete(selectedCards)

            }.addOnFailureListener { e ->
                Log.e("TAG", "Error getting selected cards", e)
                onComplete(emptyList())
            }
    }

    fun getPlayerScore(roomId: String, playerName: String, onComplete: (Int) -> Unit){
        val roomRef = db.collection("game_rooms").document(roomId)

        val players = roomRef.collection("players")

        players
            .whereEqualTo("playerName", playerName)
            .get()
            .addOnSuccessListener { playerSnapshot ->
                for (playerDocument in playerSnapshot.documents) {
                    val score = playerDocument.getString("score")?.toInt() ?: 0
                    onComplete(score)
                    return@addOnSuccessListener
                }
                onComplete(0)
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error getting players", e)
                onComplete(0)
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