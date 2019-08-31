package com.dkrasnov.slice.di

import android.app.Application
import com.dkrasnov.slice.actors.di.ActorsModule
import com.dkrasnov.slice.game.di.GameModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ActorsModule::class, GameModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setApplication(application: Application): Builder

        fun build(): AppComponent
    }
}