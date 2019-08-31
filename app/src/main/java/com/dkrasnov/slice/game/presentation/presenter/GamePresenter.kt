package com.dkrasnov.slice.game.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.dkrasnov.slice.base.SlidePresenter
import com.dkrasnov.slice.di.ComponentHolder
import com.dkrasnov.slice.game.domain.interactor.IGameInteractor
import com.dkrasnov.slice.game.presentation.view.IGameView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class GamePresenter : SlidePresenter<IGameView>() {

    @Inject
    lateinit var gameInteractor: IGameInteractor

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
                viewState.showGameOveraly()
//                viewState.showActor(it.first())
            }, {
                viewState.showLoadGameError()
            }).untilDestroy()
    }
}