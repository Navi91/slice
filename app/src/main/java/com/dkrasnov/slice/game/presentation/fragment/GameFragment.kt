package com.dkrasnov.slice.game.presentation.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.dkrasnov.slice.R
import com.dkrasnov.slice.actors.data.model.Actor
import com.dkrasnov.slice.base.SlideFragment
import com.dkrasnov.slice.extensions.log
import com.dkrasnov.slice.extensions.setVisible
import com.dkrasnov.slice.extensions.toPx
import com.dkrasnov.slice.game.presentation.presenter.GamePresenter
import com.dkrasnov.slice.game.presentation.view.IGameView
import com.dkrasnov.slice.glide.GlideApp
import kotlinx.android.synthetic.main.f_game.*
import kotlinx.android.synthetic.main.v_actor_item.view.*

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
            removeCurrentActorView()
            presenter.onThronesSelected()
        }
        ringsView.setOnClickListener {
            removeCurrentActorView()
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

    private var currentActorView: View? = null
    private var nextActorView: View? = null

    override fun addActor(actor: Actor) {
        log("show actor $actor")

        val view = LayoutInflater.from(context).inflate(R.layout.v_actor_item, contentLayout, false)
        view.id = View.generateViewId()

        if (currentActorView == null) {
            currentActorView = view
        } else {
            nextActorView = view
        }

        view.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0).apply {
            marginStart = requireContext().toPx(24f)
            marginEnd = requireContext().toPx(24f)
        }

        if (view == nextActorView) {
            contentLayout.addView(view, 1)
        } else {
            contentLayout.addView(view)
        }

        constaitActorImage(view.id)

        GlideApp.with(this).load(actor.getAssetUri()).downsample(DownsampleStrategy.AT_LEAST).into(view.actorImageView)
    }

    private fun constaitActorImage(@IdRes id: Int) {
        val set = ConstraintSet()
        set.clone(contentLayout)
        set.connect(id, ConstraintSet.TOP, taskTextView.id, ConstraintSet.BOTTOM, 24)
        set.connect(id, ConstraintSet.BOTTOM, ringsView.id, ConstraintSet.TOP, 24)

        set.applyTo(contentLayout)
    }

    private fun removeCurrentActorView() {
        currentActorView?.let { view ->
            contentLayout.removeView(view)
            currentActorView = nextActorView
            nextActorView = null
        }
    }

    override fun showGameOverlay() {
        TransitionManager.beginDelayedTransition(view as ViewGroup)

        gameOverlayGroup.setVisible(true)
    }

    override fun showGameResults() {
//        val height = actorImageView.measuredHeight.toFloat()
//        val width = actorImageView.measuredWidth.toFloat()

        listener?.onRequestGameResults(1.2f)
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
            play(createScaleViewAnimator(thronesView, thronesScale)).with(
                createScaleViewAnimator(
                    ringsView,
                    ringsScale
                )
            )
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