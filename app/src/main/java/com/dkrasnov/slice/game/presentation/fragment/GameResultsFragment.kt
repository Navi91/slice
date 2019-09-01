package com.dkrasnov.slice.game.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.dkrasnov.slice.R
import com.dkrasnov.slice.base.SlideFragment
import com.dkrasnov.slice.extensions.setVisible
import com.dkrasnov.slice.game.domain.model.PlayerChoice
import com.dkrasnov.slice.game.presentation.presenter.GameResultsPresenter
import com.dkrasnov.slice.game.presentation.view.IGameResultsView
import kotlinx.android.synthetic.main.f_game.progressBar
import kotlinx.android.synthetic.main.f_game_results.*

class GameResultsFragment : SlideFragment(), IGameResultsView {

    @InjectPresenter
    lateinit var presenter: GameResultsPresenter

    private var listener: GameResultsFragmentListener? = null

    companion object {

        fun newInstance() = GameResultsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_game_results, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun setRightAnswersCount(rightAnswerCount: Int, allAnswerCount: Int) {
        answersTextView.text = "$rightAnswerCount / $allAnswerCount"
    }

    override fun setPlayerChoiceList(playerChoiceList: List<PlayerChoice>) {

    }

    override fun setProgress(progress: Boolean) {
        TransitionManager.beginDelayedTransition(view as ViewGroup)

        progressBar.setVisible(progress)
    }

    override fun showLoadGameResultsError() {
        Toast.makeText(context, R.string.error_load_game_results, Toast.LENGTH_LONG).show()
    }

    override fun showGame() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setListener(listener: GameResultsFragmentListener) {
        this.listener = listener
    }

    interface GameResultsFragmentListener {
        fun onRequestGame()
    }
}