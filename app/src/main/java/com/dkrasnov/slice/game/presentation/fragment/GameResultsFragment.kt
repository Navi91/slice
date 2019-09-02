package com.dkrasnov.slice.game.presentation.fragment

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.dkrasnov.slice.R
import com.dkrasnov.slice.base.SlideFragment
import com.dkrasnov.slice.extensions.setVisible
import com.dkrasnov.slice.game.domain.model.PlayerChoice
import com.dkrasnov.slice.game.presentation.adapter.PlayerChoiceAdapter
import com.dkrasnov.slice.game.presentation.presenter.GameResultsPresenter
import com.dkrasnov.slice.game.presentation.view.IGameResultsView
import kotlinx.android.synthetic.main.f_game.progressBar
import kotlinx.android.synthetic.main.f_game_results.*

class GameResultsFragment : SlideFragment(), IGameResultsView {

    @InjectPresenter
    lateinit var presenter: GameResultsPresenter

    private var playerChoiceAdapter: PlayerChoiceAdapter? = null
    private var imageRatio: Float = 0f
    private var listener: GameResultsFragmentListener? = null

    companion object {

        private const val IMAGE_RATIO_BUNDLE = "image_ratio_bundle"

        fun newInstance(ratio: Float) = GameResultsFragment().apply {
            arguments = Bundle().apply {
                putFloat(IMAGE_RATIO_BUNDLE, ratio)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageRatio = arguments?.getFloat(IMAGE_RATIO_BUNDLE) ?: 0f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f_game_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAgainButton.setOnClickListener {
            presenter.onPlayAgain()
        }

        if (playerChoiceAdapter == null) {
            playerChoiceAdapter = PlayerChoiceAdapter(imageRatio)

            recyclerView.apply {
                adapter = playerChoiceAdapter
                layoutManager = GridLayoutManager(context, 2)
                addItemDecoration(createItemDecoration())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setRightAnswersCount(rightAnswerCount: Int, allAnswerCount: Int) {
        answersTextView.text = "$rightAnswerCount / $allAnswerCount"
    }

    override fun setPlayerChoiceList(playerChoiceList: List<PlayerChoice>) {
        playerChoiceAdapter?.run {
            setItems(playerChoiceList)
            notifyDataSetChanged()
        }
    }

    override fun setProgress(progress: Boolean) {
        TransitionManager.beginDelayedTransition(view as ViewGroup)

        progressBar.setVisible(progress)
    }

    override fun showLoadGameResultsError() {
        Toast.makeText(context, R.string.error_load_game_results, Toast.LENGTH_LONG).show()
    }

    override fun showGame() {
        listener?.onRequestGame()
    }

    override fun setPlayAgainEnabled(enabled: Boolean) {
        playAgainButton.isEnabled = enabled
    }

    fun setListener(listener: GameResultsFragmentListener) {
        this.listener = listener
    }

    private fun createItemDecoration(): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val position = parent.getChildAdapterPosition(view)

                if (position % 2 == 0) {
                    outRect.left = resources.getDimensionPixelSize(R.dimen.player_choice_left_edge_padding)
                    outRect.right = resources.getDimensionPixelSize(R.dimen.player_choice_right_padding)
                } else {
                    outRect.left = resources.getDimensionPixelSize(R.dimen.player_choice_left_padding)
                    outRect.right = resources.getDimensionPixelSize(R.dimen.player_choice_right_edge_padding)
                }

                outRect.top = resources.getDimensionPixelSize(R.dimen.player_choice_top_padding)
                outRect.bottom = resources.getDimensionPixelSize(R.dimen.player_choice_bottom_padding)
            }
        }
    }

    interface GameResultsFragmentListener {
        fun onRequestGame()
    }
}