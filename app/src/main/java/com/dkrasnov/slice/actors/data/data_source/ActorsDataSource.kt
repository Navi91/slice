package com.dkrasnov.slice.actors.data.data_source

import android.content.Context
import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.actors.data.model.GameInfo
import com.google.gson.Gson
import io.reactivex.Single
import java.nio.charset.Charset
import javax.inject.Inject

class ActorsDataSource @Inject constructor(
    private val context: Context,
    private val gson: Gson
) : IActorsDataSource {

    companion object {

        private const val GAME_JSON_NAME = "game.json"
    }

    override fun getActors(): Single<List<Actor>> {
        return Single.fromCallable {

            val gameInfo = gson.fromJson(getGameJson(), GameInfo::class.java)

            val actors = gameInfo.gameSerials.flatMap { gameSerial ->
                gameSerial.actors.map { gameActor ->
                    Actor.createFrom(gameActor, gameSerial.serial)
                }
            }

            actors
        }
    }

    private fun getGameJson(): String {
        var json = ""

        context.assets.open(GAME_JSON_NAME).use { stream ->
            val size = stream.available()
            val buffer = ByteArray(size)

            stream.read(buffer)

            json = String(buffer, Charset.forName("UTF-8"))
        }

        return json
    }
}