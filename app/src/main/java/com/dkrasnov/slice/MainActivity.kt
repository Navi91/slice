package com.dkrasnov.slice

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dkrasnov.slice.base.SlideActivity
import com.dkrasnov.slice.game.presentation.fragment.GameFragment
import com.dkrasnov.slice.game.presentation.fragment.GameResultsFragment

class MainActivity : SlideActivity(), GameFragment.GameFragmentLister, GameResultsFragment.GameResultsFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)

        if (supportFragmentManager.findFragmentById(R.id.containerLayout) == null) {
            showGameFragment()
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)

        if (fragment is GameFragment) {
            fragment.setListener(this)
        } else if (fragment is GameResultsFragment) {
            fragment.setListener(this)
        }
    }

    override fun onRequestGameResults(ratio: Float) {
        replaceFragment(R.id.containerLayout, GameResultsFragment.newInstance(ratio))
    }

    override fun onRequestGame() {
        showGameFragment()
    }

    private fun showGameFragment() {
        replaceFragment(R.id.containerLayout, GameFragment.newInstance())
    }
}
