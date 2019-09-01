package com.dkrasnov.slice.game.domain.interactor

import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.actors.data.model.Serial
import com.dkrasnov.slice.game.domain.model.PlayerChoice
import io.reactivex.Single

interface IGameInteractor {

    fun createGameSet(): Single<List<Actor>>

    fun selectSerialForActor(actor: Actor, selectedSerial: Serial)

    fun getGameResult(): Single<List<PlayerChoice>>
}