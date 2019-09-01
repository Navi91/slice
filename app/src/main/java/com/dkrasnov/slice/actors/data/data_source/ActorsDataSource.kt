package com.dkrasnov.slice.actors.data.data_source

import android.content.Context
import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.actors.data.model.GameInfo
import com.dkrasnov.slice.actors.data.model.Serial
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

        private const val GAME_OF_THRONES_SERIAL_NAME = "Игра Престолов"
        private const val THE_LORD_OF_RINGS_NAME = "Властелин Колец"
    }

    override fun getActors(): Single<List<Actor>> {
        return Single.fromCallable {

            val gameInfo = gson.fromJson(getGameJson(), GameInfo::class.java)

            val actors = gameInfo.gameSerials.flatMap { gameSerial ->
                gameSerial.actors.map { gameActor ->
                    Actor.createFrom(gameActor, getSerialFromName(gameSerial.serial))
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

    private fun getSerialFromName(name: String): Serial {
        return when (name) {
            GAME_OF_THRONES_SERIAL_NAME -> Serial.GAME_OF_THRONES
            THE_LORD_OF_RINGS_NAME -> Serial.THE_LORD_OF_RINGS
            else -> throw IllegalArgumentException("Unknown serial name $name")
        }
    }
}