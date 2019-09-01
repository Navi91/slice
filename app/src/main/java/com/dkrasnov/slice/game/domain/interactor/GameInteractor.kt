package com.dkrasnov.slice.game.domain.interactor

import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.actors.data.model.Serial
import com.dkrasnov.slice.game.domain.model.PlayerChoice
import io.reactivex.Single
import javax.inject.Inject

class GameInteractor @Inject constructor(
    private val repository: IActorsRepository
) : IGameInteractor {

    private val gameResultList = mutableListOf<PlayerChoice>()

    override fun createGameSet(): Single<List<Actor>> {
        return repository.getActors().map { actors ->
            val shuffleActors = actors.toMutableList()

            shuffleActors.shuffle()

            shuffleActors.toList()
        }.doOnSuccess {
            clearPreviousGame()
        }
    }

    override fun selectSerialForActor(actor: Actor, selectedSerial: Serial) {
        gameResultList.add(PlayerChoice(actor, selectedSerial))
    }

    override fun getGameResult(): Single<List<PlayerChoice>> {
        return Single.just(gameResultList)
    }

    private fun clearPreviousGame() {
        gameResultList.clear()
    }
}