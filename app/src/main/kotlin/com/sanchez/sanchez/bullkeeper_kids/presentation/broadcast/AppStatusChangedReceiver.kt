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

        when {
            intent.action == Intent.ACTION_PACKAGE_ADDED -> {

                Log.d(TAG, "Package Added -> ${intent.dataString}")
                Log.d(TAG, "Data -> ${intent.data.schemeSpecificPart}")

                saveAppInstalledInteract(SaveAppInstalledInteract.Params(intent.data.schemeSpecificPart)){
                    it.either(::handleFailure, ::handleSuccess)
                }

            }
            intent.action == Intent.ACTION_PACKAGE_REMOVED -> {

                Log.d(TAG, "Package Removed -> ${intent.dataString}")
                Log.d(TAG, "Data -> ${intent.data}")

                removeInstalledPackageInteract(RemoveInstalledPackageInteract.Params(intent.data.schemeSpecificPart)){
                    it.either(::handleFailure, ::handleSuccess)
                }

            }
            else -> Log.d(TAG, "No Handle action ->  ${intent.action}")
        }
    }

    /**
     * Handle Failure
     */
    private fun handleFailure(failure: Failure) {
        Log.d(TAG, "Handle Failure")
    }

    /**
     * Handle Success
     */
    private fun handleSuccess(void: Unit) {
        Log.d(TAG, "Application handled success")
    }
}
