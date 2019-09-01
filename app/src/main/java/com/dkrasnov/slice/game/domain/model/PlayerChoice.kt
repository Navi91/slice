package com.dkrasnov.slice.game.domain.model

import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.actors.data.model.Serial

data class PlayerChoice(val actor: Actor, val selectedSerial: Serial) {

    val actorName = actor.name

    val actorSerial = actor.serial

    fun isRight() = actor.serial == selectedSerial

    fun getAssetUri() = actor.getAssetUri()
}