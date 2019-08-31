package com.dkrasnov.slice

import android.app.Application
import com.dkrasnov.slice.di.ComponentHolder

class SlideApp : Application() {

    override fun onCreate() {
        super.onCreate()

        ComponentHolder.init(this)
    }
}