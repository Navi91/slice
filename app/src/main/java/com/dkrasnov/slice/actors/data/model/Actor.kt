package com.dkrasnov.slice.actors.data.model

import android.net.Uri

data class Actor(val name: String, val imagePath: String, val serial: Serial) {

    companion object {

        fun createFrom(gameActor: GameActor, serial: Serial) =
            Actor(gameActor.name, gameActor.imagePath, serial)
    }

    fun getAssetUri(): Uri = Uri.parse("file:///android_asset/$imagePath.jpg")
}