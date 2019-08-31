package com.dkrasnov.slice.actors.data.data_source

import com.dkrasnov.slice.actors.data.model.Actor
import io.reactivex.Single

interface IActorsDataSource {

    fun getActors() : Single<List<Actor>>
}