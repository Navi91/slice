package com.dkrasnov.slice.game.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.actors.data.model.Serial
import com.dkrasnov.slice.base.SlidePresenter
import com.dkrasnov.slice.di.ComponentHolder
import com.dkrasnov.slice.extensions.log
import com.dkrasnov.slice.game.domain.interactor.IGameInteractor
import com.dkrasnov.slice.game.presentation.view.IGameView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class GamePresenter : SlidePresenter<IGameView>() {

    companion object {
        private const val FIRST_ACTOR_INDEX = 0
    }

    @Inject
    lateinit var gameInteractor: IGameInteractor

    private val actors = mutableListOf<Actor>()
    private var currentActorIndex = FIRST_ACTOR_INDEX
    private var nextToShowActorIndex = FIRST_ACTOR_INDEX

    init {
        ComponentHolder.applicationComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        gameInteractor.createGameSet()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.setProgress(true) }
            .doAfterTerminate { viewState.setProgress(false) }
            .subscribe({
                viewState.showGameOverlay()

                setGameActors(it)
                showNextActorIfExist()
                showNextActorIfExist()
            }, {
                log(it)

                viewState.showLoadGameError()
            }).untilDestroy()
    }

    fun onThronesSelected() {
        if (currentActorIndex < actors.size) {
            gameInteractor.selectSerialForActor(actors[currentActorIndex], Serial.GAME_OF_THRONES)
            showNextActorIfExist()
            currentActorIndex++
        }

        if (currentActorIndex == actors.lastIndex + 1) {
            endGame()
        }
    }

    fun onRingsSelected() {
        if (currentActorIndex < actors.size) {
            gameInteractor.selectSerialForActor(actors[currentActorIndex], Serial.THE_LORD_OF_RINGS)
            showNextActorIfExist()
            currentActorIndex++
        }

        if (currentActorIndex == actors.lastIndex + 1) {
            endGame()
        }
    }

    private fun setGameActors(actors: List<Actor>) {
        this.actors.apply {
            clear()
            addAll(actors)
        }

        currentActorIndex = FIRST_ACTOR_INDEX
        nextToShowActorIndex = FIRST_ACTOR_INDEX
    }

    private fun showNextActorIfExist() {
        if (nextToShowActorIndex < actors.size) {
            viewState.addActor(actors[nextToShowActorIndex])
            nextToShowActorIndex++
        }
    }

    private fun endGame() {
        viewState.showGameResults()
    }
}