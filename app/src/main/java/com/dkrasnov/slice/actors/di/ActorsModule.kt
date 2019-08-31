package com.dkrasnov.slice.actors.di

import com.dkrasnov.slice.actors.data.data_source.ActorsDataSource
import com.dkrasnov.slice.actors.data.data_source.IActorsDataSource
import com.dkrasnov.slice.actors.data.repository.ActorsRepository
import com.dkrasnov.slice.game.domain.interactor.IActorsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ActorsModule {

    @Binds
    @Singleton
    fun provideActorsDataSource(dataSource: ActorsDataSource) : IActorsDataSource

    @Binds
    @Singleton
    fun provideActorsRepositoty(repository: ActorsRepository) : IActorsRepository
}