package pt.isec.sofiaigp.whatdoyoumeme.data

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager() {

    private val db = FirebaseFirestore.getInstance()

    fun createPlayer(playerName: String, score: Int? = 0, onSuccess: () -> Unit, onFailure: () -> Unit) {

        db.collection("players")
            .whereEqualTo("playerName", playerName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    val playerData = hashMapOf(
                        "playerName" to playerName,
                        "score" to score
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


    fun createGameRoom(roomName: String, maxPlayers: Int, numRounds: Int, playerName: String) {
        val gameRoom = hashMapOf(
            "roomName" to roomName,
            "players" to emptyList<String>(),
            "maxPlayers" to maxPlayers,
            "numRounds" to numRounds
        )

        db.collection("game_rooms")
            .add(gameRoom)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "Game room created with ID: ${documentReference.id}")
                joinGameRoom(playerName, documentReference.id, maxPlayers)
            }.addOnFailureListener { exception ->
                Log.e("TAG", "Error creating game room", exception)
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

                        val roomRef = db.collection("game_rooms").document(roomId)
                        roomRef.get()
                            .addOnSuccessListener { roomSnapshot ->
                                val playersList = roomSnapshot.get("players") as List<String>
                                if (playersList.size < maxPlayers) {
                                    roomRef.update("players", FieldValue.arrayUnion(playerId))
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
                    val players = document.get("players") as List<String>
                    val maxPlayers = document.getLong("maxPlayers")?.toInt()
                    val currentNumPlayers = players.size
                    val numRounds = document.getLong("numRounds")?.toInt()
                    val gameRoom = GameRoom(
                        roomId,
                        roomName,
                        players,
                        maxPlayers,
                        currentNumPlayers,
                        numRounds
                    )
                    gameRooms.add(gameRoom)
                }
                onUpdate(gameRooms)
            }
        }
    }

}