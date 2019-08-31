package com.dkrasnov.slice.actors.data.data_source

import android.content.Context
import com.dkrasnov.slice.actors.data.model.Actor
import io.reactivex.Single
import javax.inject.Inject

class ActorsDataSource @Inject constructor(private val context: Context): IActorsDataSource {

    override fun getActors(): Single<List<Actor>> {
        return Single.just(emptyList())
    }
}