package com.dkrasnov.slice.game.domain

import com.dkrasnov.slice.game.domain.IActorsRepository
import javax.inject.Inject

class GameInteractor @Inject constructor(private val repository: IActorsRepository) : IGameInteractor {
}