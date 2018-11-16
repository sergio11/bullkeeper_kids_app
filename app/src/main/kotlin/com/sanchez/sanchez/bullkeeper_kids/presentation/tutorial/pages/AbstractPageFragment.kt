package com.sanchez.sanchez.bullkeeper_kids.presentation.tutorial.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleveroad.slidingtutorial.PageSupportFragment

/**
 * Abstract Page Fragment
 */
abstract class AbstractPageFragment<T>: PageSupportFragment() {

    var component: T? = null

    /**
     * On Create View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        component = initializeInjector()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * Initialize Injector
     */
    abstract fun initializeInjector(): T?


    /**
     * When Phase is Hidden
     */
    abstract fun whenPhaseIsHidden(pagePosition: Int, currentPosition: Int)

    /**
     * When Phase is showed
     */
    abstract fun whenPhaseIsShowed()

}