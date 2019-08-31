package com.dkrasnov.slice

import android.os.Bundle
import com.dkrasnov.slice.base.SlideActivity
import com.dkrasnov.slice.game.presentation.fragment.GameFragment

class MainActivity : SlideActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)

        if (supportFragmentManager.findFragmentById(R.id.containerLayout) == null) {
            replaceFragment(R.id.containerLayout, GameFragment.newInstance())
        }
    }
}
