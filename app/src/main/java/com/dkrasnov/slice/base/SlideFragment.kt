package com.dkrasnov.slice.base

import com.arellomobile.mvp.MvpAppCompatFragment

open class SlideFragment : MvpAppCompatFragment() {

    fun getSldeActivity(): SlideActivity? {
        return activity as? SlideActivity
    }
}