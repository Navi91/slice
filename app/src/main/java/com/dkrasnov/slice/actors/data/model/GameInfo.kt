package com.dkrasnov.slice.actors.data.model

import com.google.gson.annotations.SerializedName

data class GameInfo(@SerializedName("game_items") val gameSerials: List<GameSerial>)