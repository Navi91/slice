package com.dkrasnov.slice.extensions

import android.content.Context
import android.util.DisplayMetrics


fun Context.toPx(dp: Float): Int {
    return (dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}