package com.dkrasnov.slice.game.presentation.fragment

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.dkrasnov.slice.R
import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.base.SlideFragment
import com.dkrasnov.slice.extensions.log
import com.dkrasnov.slice.extensions.setVisible
import com.dkrasnov.slice.game.presentation.presenter.GamePresenter
import com.dkrasnov.slice.game.presentation.view.IGameView
import kotlinx.android.synthetic.main.f_game.*

class GameFragment : SlideFragment(), IGameView {

    companion object {

        fun newInstance() = GameFragment()
    }

    @InjectPresenter
    lateinit var presenter: GamePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_game, container, false)
    }

    override fun showLoadGameError() {
        log("show load game error")

        Toast.makeText(context, getString(R.string.error_load_game), Toast.LENGTH_LONG).show()
    }

    override fun showActor(actor: Actor) {
        log("show actor $actor")
    }

    override fun showGameOveraly() {
        TransitionManager.beginDelayedTransition(view as ViewGroup)

        gameOverlayGroup.setVisible(true)
    }

    override fun setProgress(progress: Boolean) {
        log("set progress $progress")

        TransitionManager.beginDelayedTransition(view as ViewGroup)

        progressBar.setVisible(progress)
    }
}