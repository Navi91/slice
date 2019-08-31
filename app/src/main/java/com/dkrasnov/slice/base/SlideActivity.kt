package com.dkrasnov.slice.base

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

@SuppressLint("Registered")
open class SlideActivity : AppCompatActivity() {

    protected fun replaceFragment(@IdRes id: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(id, fragment).commitNow()
    }
}