package com.dkrasnov.slice.game.presentation.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.dkrasnov.slice.R
import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.base.SlideFragment
import com.dkrasnov.slice.extensions.log
import com.dkrasnov.slice.extensions.setVisible
import com.dkrasnov.slice.game.presentation.presenter.GamePresenter
import com.dkrasnov.slice.game.presentation.view.IGameView
import com.dkrasnov.slice.glide.GlideApp
import kotlinx.android.synthetic.main.f_game.*

class GameFragment : SlideFragment(), IGameView {

    companion object {

        private const val SCALE_BUTTON_DURATION = 250L

        fun newInstance() = GameFragment()
    }

    @InjectPresenter
    lateinit var presenter: GamePresenter

    private var listener: GameFragmentLister? = null
    private var currentAnimator: Animator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thronesView.setOnClickListener {
            presenter.onThronesSelected()
        }
        ringsView.setOnClickListener {
            presenter.onRingsSelected()
        }

        thronesView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                log("throne down")
                scaleUpThronesView()
            } else if (event.action == MotionEvent.ACTION_UP) {
                log("throne up")
                normalizeScale()
            }

            return@setOnTouchListener false
        }
        ringsView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                log("ring down")
                scaleUpRingsView()
            } else if (event.action == MotionEvent.ACTION_UP) {
                log("ring up")
                normalizeScale()
            }

            return@setOnTouchListener false
        }
    }

    override fun showLoadGameError() {
        log("show load game error")

        Toast.makeText(context, getString(R.string.error_load_game), Toast.LENGTH_LONG).show()
    }

    override fun showActor(actor: Actor) {
        log("show actor $actor")

        GlideApp.with(this).load(actor.getAssetUri()).downsample(DownsampleStrategy.AT_LEAST).into(actorImageView)
    }

    override fun showGameOverlay() {
        TransitionManager.beginDelayedTransition(view as ViewGroup)

        gameOverlayGroup.setVisible(true)
    }

    override fun showGameResults() {
        val height = actorImageView.measuredHeight.toFloat()
        val width = actorImageView.measuredWidth.toFloat()

        listener?.onRequestGameResults(height / width)
    }

    override fun setProgress(progress: Boolean) {
        log("set progress $progress")

        TransitionManager.beginDelayedTransition(view as ViewGroup)

        progressBar.setVisible(progress)
    }

    fun setListener(listener: GameFragmentLister) {
        this.listener = listener
    }

    private fun scaleUpThronesView() {
        updateViewScales(1.1f, 0.9f)
    }

    private fun scaleUpRingsView() {
        updateViewScales(0.9f, 1.1f)
    }

    private fun normalizeScale() {
        updateViewScales(1f, 1f)
    }

    private fun updateViewScales(thronesScale: Float, ringsScale: Float) {
        currentAnimator?.cancel()
        currentAnimator = AnimatorSet().apply {
            play(createScaleViewAnimator(thronesView, thronesScale)).with(createScaleViewAnimator(ringsView, ringsScale))
        }
        currentAnimator?.start()
    }

    private fun createScaleViewAnimator(view: View, scale: Float): Animator {
        val scaleDownX = ObjectAnimator.ofFloat(view, View.SCALE_X, scale).apply {
            duration = SCALE_BUTTON_DURATION
        }
        val scaleDownY = ObjectAnimator.ofFloat(view, View.SCALE_Y, scale).apply {
            duration = SCALE_BUTTON_DURATION
        }

        return AnimatorSet().apply {
            play(scaleDownX).with(scaleDownY)
        }
    }

    interface GameFragmentLister {
        fun onRequestGameResults(ratio: Float)
    }
}