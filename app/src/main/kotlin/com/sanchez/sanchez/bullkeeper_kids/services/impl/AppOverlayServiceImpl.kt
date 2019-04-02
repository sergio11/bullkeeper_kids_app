package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.view.*
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.services.IAppOverlayService
import timber.log.Timber



/**
 App Overlay Service Impl
 **/
class AppOverlayServiceImpl
    constructor(private val appContext: Context): IAppOverlayService {

    private val uiHandler: Handler by lazy {
        Handler(appContext.mainLooper)
    }

    private var currentViewShowed: View? = null


    /**
     * Create
     * @param appOverlayLayout
     */
    override fun create(appOverlayLayout: Int): View? {
        var appOverlayView: View? = null
        try {
            val layoutInflater = appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            appOverlayView = layoutInflater.inflate(appOverlayLayout, null)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return appOverlayView
    }

    /**
     * Show
     * @param view
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun show(view: View) {
        try {
            val mWindowManager = appContext.getSystemService(WINDOW_SERVICE) as WindowManager
            if (currentViewShowed != null)
                currentViewShowed?.let { hide(it) }
            currentViewShowed = view
            uiHandler.post {
                Timber.d("Thread Name -> %s", Thread.currentThread().name)
                mWindowManager.addView(view, createCommonLayoutParams())
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    /**
     * Hide
     * @param view
     */
    override fun hide(view: View) {
        currentViewShowed = null
        try {
            val mWindowManager = appContext.getSystemService(WINDOW_SERVICE) as WindowManager
            mWindowManager?.removeView(view)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    /**
     * Can Draw Overlays
     */
    override fun canDrawOverlays(): Boolean =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(appContext)


     /**
     * Private Methods
     */

    /**
     * Create Common Layout Params
     * @return
     */
    private fun createCommonLayoutParams(): WindowManager.LayoutParams {

        val params = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    PixelFormat.TRANSLUCENT)
            else
                WindowManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS),
                    PixelFormat.TRANSLUCENT)

            params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            params.x = 0
            params.y = 100
            params.windowAnimations = R.anim.anim_in

            return params
    }
}