package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.RemoveInstalledPackageInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SaveAppInstalledInteract
import javax.inject.Inject

/**
 * App Status Changed Receiver
 */
class AppStatusChangedReceiver : BroadcastReceiver() {

    val TAG = "APP_STATUS"

    private val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.appComponent
    }

    companion object {

        /**
         * Get Intent Filter
         */
        @JvmStatic
        fun getIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
            intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
            intentFilter.addDataScheme("package")
            return intentFilter
        }

    }


    /**
     * Save Installed Package Interact
     */
    @Inject lateinit var saveAppInstalledInteract: SaveAppInstalledInteract

    /**
     * Remove Installed Package Interact
     */
    @Inject lateinit var removeInstalledPackageInteract: RemoveInstalledPackageInteract


    /**
     * On Receive
     */
    override fun onReceive(context: Context, intent: Intent) {
        appComponent.inject(this)

        if(intent.action.equals(Intent.ACTION_PACKAGE_ADDED)) {

            Log.d(TAG, "Package Added -> ${intent.dataString}")
            Log.d(TAG, "Data -> ${intent.data}")

            saveAppInstalledInteract(SaveAppInstalledInteract.Params(intent.dataString)){
                it.either(::handleFailure, ::handleSuccess)
            }

        } else if(intent.action.equals(Intent.ACTION_PACKAGE_REMOVED)) {

            Log.d(TAG, "Package Removed -> ${intent.dataString}")
            Log.d(TAG, "Data -> ${intent.data}")

            removeInstalledPackageInteract(RemoveInstalledPackageInteract.Params(intent.dataString)){
                it.either(::handleFailure, ::handleSuccess)
            }

        } else {
            Log.d(TAG, "Other Result")
        }
    }

    /**
     * Handle Failure
     */
    private fun handleFailure(failure: Failure) {
        Log.d(TAG, "Handle Failure")
    }

    private fun handleSuccess(applicationName: String) {
        Log.d(TAG, "Application $applicationName handled")
    }
}
