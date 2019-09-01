package com.dkrasnov.slice.game.presentation.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.dkrasnov.slice.R
import com.dkrasnov.slice.actors.data.model.Serial
import com.dkrasnov.slice.extensions.setVisible
import com.dkrasnov.slice.game.domain.model.PlayerChoice
import com.dkrasnov.slice.glide.GlideApp
import kotlinx.android.synthetic.main.v_player_choice_item.view.*

class PlayerChoiceAdapter(
    private val imageRatio: Float
) : RecyclerView.Adapter<PlayerChoiceAdapter.PlayerChoiceViewHolder>() {

    private val items = mutableListOf<PlayerChoice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerChoiceViewHolder {
        val context = parent.context
        val resources = context.resources

        val view = LayoutInflater.from(context).inflate(
            R.layout.v_player_choice_item, parent, false
        )

        val params = view.layoutParams as GridLayoutManager.LayoutParams

        val paddings = resources.getDimensionPixelSize(R.dimen.player_choice_left_edge_padding) +
                resources.getDimensionPixelSize(R.dimen.player_choice_left_padding) +
                resources.getDimensionPixelSize(R.dimen.player_choice_right_edge_padding) +
                resources.getDimensionPixelSize(R.dimen.player_choice_right_padding)

        val height = ((parent.measuredWidth - paddings) / 2) * imageRatio

        params.height = height.toInt()

        return PlayerChoiceViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PlayerChoiceViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<PlayerChoice>) {
        this.items.apply {
            clear()
            addAll(items)
        }
    }

    class PlayerChoiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: PlayerChoice) {
            with(itemView) {
                GlideApp.with(actorImageView).load(item.getAssetUri()).downsample(DownsampleStrategy.AT_LEAST)
                    .into(actorImageView)

                if (item.isRight()) {
                    resultButton.setText(R.string.right)
                    resultButton.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.rightAnswerBackgroundColor)
                    selectedSerialTextView.paintFlags =
                        selectedSerialTextView.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
                } else {
                    resultButton.setText(R.string.wrong)
                    resultButton.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.wrongAnswerBackgroundColor)
                    selectedSerialTextView.paintFlags =
                        selectedSerialTextView.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)
                }
                actorNameTextView.text = item.actorName
                selectedSerialTextView.text = item.selectedSerial.getName()
                rightSerialTextView.setVisible(!item.isRight())
                rightSerialTextView.text = item.actorSerial.getName()
            }
        }

        private fun Serial.getName(): String {
            return when (this) {
                Serial.GAME_OF_THRONES -> itemView.context.getString(R.string.game_of_thrones)
                Serial.THE_LORD_OF_RINGS -> itemView.context.getString(R.string.the_lord_of_rings)
                else -> itemView.context.getString(R.string.unknown_serial)
            }
        }
    }

}