package com.dkrasnov.slice.extensions

import android.view.View

fun View.visible() {
    this.setVisible(true)
}

fun View.gone() {
    this.setVisible(false)
}

fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}
