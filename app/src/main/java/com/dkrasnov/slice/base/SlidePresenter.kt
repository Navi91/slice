package com.dkrasnov.slice.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class SlidePresenter<T : SlideView> : MvpPresenter<T>() {

    private val disposables = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()

        disposables.clear()
    }

    protected fun Disposable.untilDestroy() {
        disposables.add(this)
    }
}