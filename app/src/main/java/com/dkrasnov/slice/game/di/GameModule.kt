package com.dkrasnov.slice.game.di

import com.dkrasnov.slice.game.domain.interactor.GameInteractor
import com.dkrasnov.slice.game.domain.interactor.IGameInteractor
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface GameModule {

    @Binds
    @Singleton
    fun provideGameInteractor(interact: GameInteractor): IGameInteractor
}