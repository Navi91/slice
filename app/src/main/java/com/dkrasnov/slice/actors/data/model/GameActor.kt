package com.dkrasnov.slice.actors.data.model

import com.google.gson.annotations.SerializedName

data class GameActor(val name: String, @SerializedName("image") val imagePath: String)