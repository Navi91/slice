package com.dkrasnov.slice.actors.data.model

import com.google.gson.annotations.SerializedName

data class GameSerial(val serial: String, @SerializedName("items") val actors: List<GameActor>)