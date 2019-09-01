package com.dkrasnov.slice.game.presentation.view

import com.arellomobile.mvp.viewstate.strategy.*
import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.base.SlideView

interface IGameView: SlideView {
    
    @StateStrategyType(SkipStrategy::class)
    fun showLoadGameError()
    
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showActor(actor: Actor)
    
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProgress(progress: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showGameOverlay()

    @StateStrategyType(SingleStateStrategy::class)
    fun showGameResults()
}