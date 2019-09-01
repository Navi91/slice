package com.dkrasnov.slice.game.presentation.view

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.dkrasnov.slice.base.SlideView
import com.dkrasnov.slice.game.domain.model.PlayerChoice

interface IGameResultsView : SlideView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setRightAnswersCount(rightAnswerCount: Int, allAnswerCount: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setPlayerChoiceList(playerChoiceList: List<PlayerChoice>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProgress(progress: Boolean)

    @StateStrategyType(SkipStrategy::class)
    fun showLoadGameResultsError()

    @StateStrategyType(SingleStateStrategy::class)
    fun showGame()
}