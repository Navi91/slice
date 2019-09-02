package com.dkrasnov.slice.game.presentation.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
        private const val BUTTON_UP_SCALE = 1.1f
        private const val BUTTON_DOWN_SCALE = 0.9f
        private const val BUTTON_NORMAL_SCALE = 1f
        private const val ACTOR_VIEW_REMOVE_DURATION = 350L
        private const val ACTORE_VIEW_REMOVE_ROTATION = 20f
        private const val ACTORE_VIEW_DRAG_ROTATION = 5f

        fun newInstance() = GameFragment()
    }

    @InjectPresenter
    lateinit var presenter: GamePresenter

    private var listener: GameFragmentLister? = null
    private var currentAnimator: Animator? = null
    private var actorViewRatio: Float = 1f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thronesButtonView.setOnClickListener {
            setButtonEnabled(false)

            thronesAnimateActorViewRemove {
                setButtonEnabled(true)

                removeCurrentActorView()
                presenter.onThronesSelected()
            }
        }
        ringsButtonView.setOnClickListener {
            setButtonEnabled(false)

            ringsAnimateActorViewRemove {
                setButtonEnabled(true)

                removeCurrentActorView()
                presenter.onRingsSelected()
            }
        }

        thronesButtonView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                log("throne down")
                scaleUpThronesView()
            } else if (event.action == MotionEvent.ACTION_UP) {
                log("throne up")
                normalizeScale()
            }

            return@setOnTouchListener false
        }
        ringsButtonView.setOnTouchListener { _, event ->
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

        constraitActorImage(view.id)

        GlideApp.with(this).load(actor.getAssetUri()).downsample(DownsampleStrategy.AT_LEAST).into(view.actorImageView)

        updateActorViewRatio()
    }

    private fun constraitActorImage(@IdRes id: Int) {
        val set = ConstraintSet()
        set.clone(contentLayout)
        set.connect(id, ConstraintSet.TOP, taskTextView.id, ConstraintSet.BOTTOM, requireContext().toPx(24f))
        set.connect(id, ConstraintSet.BOTTOM, ringsButtonView.id, ConstraintSet.TOP, requireContext().toPx(24f))

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
        listener?.onRequestGameResults(actorViewRatio)
    }

    override fun setProgress(progress: Boolean) {
        log("set progress $progress")

        TransitionManager.beginDelayedTransition(view as ViewGroup)

        progressBar.setVisible(progress)
    }

    fun setListener(listener: GameFragmentLister) {
        this.listener = listener
    }

    private fun ringsAnimateActorViewRemove(callback: () -> Unit) {
        animateActorViewRemove(
            ACTORE_VIEW_REMOVE_ROTATION,
            contentLayout.width.toFloat(),
            0f,
            currentActorView?.height?.toFloat() ?: 0f,
            callback
        )
    }

    private fun thronesAnimateActorViewRemove(callback: () -> Unit) {
        animateActorViewRemove(
            -ACTORE_VIEW_REMOVE_ROTATION,
            -contentLayout.width.toFloat(),
            currentActorView?.width?.toFloat() ?: 0f,
            currentActorView?.height?.toFloat() ?: 0f,
            callback
        )
    }

    private fun animateActorViewRemove(
        rotation: Float,
        translationX: Float,
        privotX: Float,
        pivotY: Float,
        callback: () -> Unit
    ) {
        currentActorView?.let { view ->
            view.pivotX = privotX
            view.pivotY = pivotY

            val rotationAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, rotation)
            val translationAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translationX)

            AnimatorSet().apply {
                play(translationAnimator).with(rotationAnimator)
                duration = ACTOR_VIEW_REMOVE_DURATION
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator?) {
                        callback()
                    }
                })
            }.start()
        }
    }

    private fun scaleUpThronesView() {
        updateViewScales(BUTTON_UP_SCALE, BUTTON_DOWN_SCALE)
        animateActorViewDrag(
            -ACTORE_VIEW_DRAG_ROTATION,
            currentActorView?.width?.toFloat() ?: 0f,
            currentActorView?.height?.toFloat() ?: 0f
        )
    }

    private fun scaleUpRingsView() {
        updateViewScales(BUTTON_DOWN_SCALE, BUTTON_UP_SCALE)
        animateActorViewDrag(
            ACTORE_VIEW_DRAG_ROTATION,
            0f,
            currentActorView?.height?.toFloat() ?: 0f
        )
    }

    private fun normalizeScale() {
        updateViewScales(BUTTON_NORMAL_SCALE, BUTTON_NORMAL_SCALE)
        animateActorViewDrag(
            0f,
            currentActorView?.pivotX ?: 0f,
            currentActorView?.pivotY ?: 0f
        )
    }

    private var actorDragAnimator: Animator? = null

    private fun animateActorViewDrag(rotation: Float, pivotX: Float, pivotY: Float) {
        currentActorView?.let { view ->
            view.pivotX = pivotX
            view.pivotY = pivotY

            actorDragAnimator?.cancel()
            actorDragAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, rotation).apply {
                duration = SCALE_BUTTON_DURATION
            }
            actorDragAnimator?.start()
        }
    }

    private fun updateViewScales(thronesScale: Float, ringsScale: Float) {
        currentAnimator?.cancel()
        currentAnimator = AnimatorSet().apply {
            play(createScaleViewAnimator(thronesButtonView, thronesScale)).with(
                createScaleViewAnimator(
                    ringsButtonView,
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

    private fun setButtonEnabled(enabled: Boolean) {
        thronesButtonView.isEnabled = enabled
        ringsButtonView.isEnabled = enabled
    }

    private fun updateActorViewRatio() {
        currentActorView?.let { view ->
            val height = view.measuredHeight.toFloat()
            val width = view.measuredWidth.toFloat()

            actorViewRatio = height / width
        }
    }

    interface GameFragmentLister {
        fun onRequestGameResults(ratio: Float)
    }
}