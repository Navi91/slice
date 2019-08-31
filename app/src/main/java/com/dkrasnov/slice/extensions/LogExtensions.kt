package com.dkrasnov.slice.extensions

import android.util.Log

fun Any.log(message: String) {
    Log.d(this.javaClass.simpleName, message)
}