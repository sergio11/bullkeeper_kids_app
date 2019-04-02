package com.sanchez.sanchez.bullkeeper_kids.services

import android.os.Build
import android.support.annotation.LayoutRes
import android.support.annotation.RequiresApi
import android.view.View

/**
 App Overlay Service
 **/
interface IAppOverlayService {

    /**
     * Create
     * @param appOverlayLayout
     * @return
     */
    fun create(@LayoutRes appOverlayLayout: Int): View?

    /**
     *
     * @param view
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun show(view: View)


    /**
     *
     * @param view
     */
    fun hide(view: View)

    /**
     * Can Draw Overlays
     * @return
     */
    fun canDrawOverlays(): Boolean

}