package com.dkrasnov.slice.actors.data.repository

import com.dkrasnov.slice.actors.data.data_source.IActorsDataSource
import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.game.domain.interactor.IActorsRepository
import io.reactivex.Single

class ActorsRepository(private val actorDataSource: IActorsDataSource) :
    IActorsRepository {

    override fun getActors(): Single<List<Actor>> {
        return actorDataSource.getActors()
    }
}