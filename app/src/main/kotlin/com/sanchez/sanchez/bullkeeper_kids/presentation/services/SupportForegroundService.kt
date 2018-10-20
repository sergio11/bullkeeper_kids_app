package com.sanchez.sanchez.bullkeeper_kids.presentation.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Binder
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log

/**
 * Support Foreground Service
 */
abstract class SupportForegroundService: Service(){

    private val TAG = SupportForegroundService::class.java!!.getSimpleName()

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private val NOTIFICATION_ID = 12345678


    private val mBinder = LocalBinder()

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var mChangingConfiguration = false

    private var mServiceHandler: Handler? = null


    /**
     * Get Notification
     */
    protected abstract fun getNotification(): Notification

    /**
     * Inject Dependencies
     */
    protected abstract fun injectDependencies()

    /**
    * On Create
    */
    override fun onCreate() {
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        injectDependencies()
    }

    /**
     * On Configuration Changed
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }


    /**
     * Called when a client comes to the foreground and binds with this service.
     * The service should cease to be a foreground service when that happens.
     */
    override fun onBind(intent: Intent): IBinder? {
        Log.d(TAG, "ON BIND -> Stop Foreground")
        stopForeground(true)
        mChangingConfiguration = false
        return mBinder
    }

    /**
     * Called when a client returns to the foreground
     * and binds once again with this service. The service should cease to be a foreground
     * service when that happens.
     */
    override fun onRebind(intent: Intent) {
        Log.i(TAG, "ON REBIND -> Stop Foreground")
        stopForeground(true)
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    /**
     * Called when the last client unbinds from this service.
     * If this method is called due to a configuration change in MainActivity,
     * we do nothing. Otherwise, we make this service a foreground service.
     */
    override fun onUnbind(intent: Intent): Boolean {
        Log.i(TAG, "Last client unbound from service")
        if (!mChangingConfiguration) {
            Log.i(TAG, "Starting foreground service")
            startForeground(NOTIFICATION_ID, getNotification())
        }
        return true
    }

    /**
     * On Destroy
     */
    override fun onDestroy() {
        mServiceHandler!!.removeCallbacksAndMessages(null)
    }

    /**
     * Returns true if this is a foreground service.
     */
    private fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(
                Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: SupportForegroundService
            get() = this@SupportForegroundService
    }


}