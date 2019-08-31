package com.dkrasnov.slice.game.domain.interactor

import com.dkrasnov.slice.actors.data.model.Actor
import io.reactivex.Single

interface IActorsRepository {

    fun getActors() : Single<List<Actor>>
}