package com.dkrasnov.slice.game.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.dkrasnov.slice.base.SlidePresenter
import com.dkrasnov.slice.di.ComponentHolder
import com.dkrasnov.slice.extensions.log
import com.dkrasnov.slice.game.domain.interactor.IGameInteractor
import com.dkrasnov.slice.game.presentation.view.IGameResultsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class GameResultsPresenter : SlidePresenter<IGameResultsView>() {

    @Inject
    lateinit var gameInteractor: IGameInteractor

    init {
        ComponentHolder.applicationComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        gameInteractor.getGameResult()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.setProgress(true) }
            .doAfterTerminate { viewState.setProgress(false) }
            .subscribe({ playerChoiceList ->
                val rightAnswerCount = playerChoiceList.count { it.isRight() }
                val allAnswerCount = playerChoiceList.size

                viewState.setRightAnswersCount(rightAnswerCount, allAnswerCount)
                viewState.setPlayerChoiceList(playerChoiceList)
            }, {
                log(it)

                viewState.showLoadGameResultsError()
            }).untilDestroy()
    }

    fun onGameAgain() {
        viewState.showGame()
    }
}