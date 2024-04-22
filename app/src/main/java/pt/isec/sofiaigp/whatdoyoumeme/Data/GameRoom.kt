package pt.isec.sofiaigp.whatdoyoumeme.Data

data class GameRoom(
    var name : String ?= null,
    var players : Int ?= 0,
    var maxPlayers : Int ?= 0
)
