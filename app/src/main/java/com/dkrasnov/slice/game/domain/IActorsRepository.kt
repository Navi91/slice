package com.dkrasnov.slice.game.domain

import com.dkrasnov.slice.actors.data.model.Actor
import io.reactivex.Single

interface IActorsRepository {

    fun getActors() : Single<List<Actor>>
}