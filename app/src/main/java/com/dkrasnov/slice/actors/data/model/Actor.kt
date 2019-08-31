package com.dkrasnov.slice.actors.data.model

data class Actor(val name: String, val imagePath: String, val serial: Serial) {

    companion object {

        fun createFrom(gameActor: GameActor, serialName: String) =
            Actor(gameActor.name, gameActor.imagePath, Serial(serialName))
    }
}