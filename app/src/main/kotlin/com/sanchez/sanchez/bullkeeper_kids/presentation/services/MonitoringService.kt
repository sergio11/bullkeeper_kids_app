package com.sanchez.sanchez.bullkeeper_kids.presentation.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ServiceComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.Navigator
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetAllPackagesInstalledInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.GetBlockedPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SynchronizeInstalledPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AppStatusChangedReceiver
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import java.util.*


/**
 * @author Sergio Sánchez Sánchez
 *
 * A bound and started service that is promoted to a foreground service when all clients unbind.
 *
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 *
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that service is removed.
 */
class MonitoringService : SupportForegroundService(){

    // Extra Started From Notification
    private val EXTRA_STARTED_FROM_NOTIFICATION = "STARTED_FROM_NOTIFICATION"

    private val TAG = MonitoringService::class.java.simpleName

    /**
     * Service Component
     */
    private val serviceComponent: ServiceComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.serviceComponent
    }

    /**
     * Check Foreground App Interval
     */
    private val CHECK_FOREGROUND_APP_INTERVAL: Long = 3000 // Every 3 seconds

    /**
     * Check Foreground App Timer
     */
    private var checkForegroundAppTimer = Timer()

    /**
     * Dependencies
     */

    /**
     * Local Notification Service
     */
    @Inject
    internal lateinit var localNotificationService: ILocalNotificationService

    /**
     * Get Blocked Packages Interact
     */
    @Inject
    internal lateinit var getBlockedPackagesInteract: GetBlockedPackagesInteract

    /**
     * Get All Packages Installed Interact
     */
    @Inject
    internal lateinit var getAllPackagesInstalledInteract: GetAllPackagesInstalledInteract

    /**
     * Usage Stats Service
     */
    @Inject
    internal lateinit var usageStatsService: IUsageStatsService

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: Navigator


    /**
     * Synchronize Installed Packages Interact
     */
    @Inject
    internal lateinit var synchronizeInstalledPackagesInteract:
            SynchronizeInstalledPackagesInteract


    /**
     * Receivers
     * =====================
     */

    /**
     * App Status Changed Receiver
     */
    private lateinit var appStatusChangedReceiver: AppStatusChangedReceiver

    /**
     * Screen Status Receiver
     */
    private lateinit var screenStatusReceiver: ScreenStatusReceiver

    /**
     * State
     */
    private var appBlockList: Set<SystemPackageInfo> = HashSet()


    /**
     * Get Notification
     */
    override fun getNotification(): Notification {

        val intent = Intent(this, MonitoringService::class.java)
        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        val pendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        /**
         * Get Notification
         */
        return localNotificationService.getNotification("Monitoring Service Title",
                "Monitoring Service Content", pendingIntent)
    }

    /**
     * On Create
     */
    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "On Create Monitoring Service")

        // Register App Status Broadcast receiver
        appStatusChangedReceiver = AppStatusChangedReceiver()
        registerReceiver(appStatusChangedReceiver,
                AppStatusChangedReceiver.getIntentFilter())

        // Register Screen Status Receiver
        screenStatusReceiver = ScreenStatusReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)
        registerReceiver(screenStatusReceiver, filter)


        // Get All Packages Installed
        getAllPackagesInstalledInteract(UseCase.None()){
            it.either(::onGetAllPackagesInstalledFailed, ::onGetAllPackagesInstalledSuccess)
        }

    }

    /**
     * On Destroy
     */
    override fun onDestroy() {
        Log.d(TAG, "On Destroy Monitoring Service")
        unregisterReceiver(appStatusChangedReceiver)
        unregisterReceiver(screenStatusReceiver)

        disableAppForegroundMonitoring()
    }

    /**
     * On Start Command
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "Monitoring Service started")

        val startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false)

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            stopSelf()
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return Service.START_NOT_STICKY
    }

    /**
     * Inject Dependencies
     */
    override fun injectDependencies() {
        serviceComponent.inject(this)
    }

    /**
     * On Sync Packages Failed
     */
    private fun onSyncPackagesFailed(failure: Failure) {
        Log.d(TAG, "On Sync Packages Failed")

    }

    /**
     * On Sync Packages Success
     */
    private fun onSyncPackagesSuccess(unit: Unit) {
        Log.d(TAG, "On Sync Packages Success")

    }


    /**
     * On Get All Packages Installed Failed
     */
    private fun onGetAllPackagesInstalledFailed(failure: Failure) {
        Log.d(TAG, "onGetAllPackagesInstalledFailed")
    }


    /**
     * On Get All Packages Installed Success
     */
    private fun onGetAllPackagesInstalledSuccess(packagesInstalled: List<SystemPackageInfo>) {

        if(packagesInstalled.isNotEmpty()){

            appBlockList = packagesInstalled.asSequence()
                    .filter{ it.isBlocked }.toHashSet()

            Log.d(TAG, "Packages Blocked -> ${appBlockList.size}")

            appBlockList.forEach{ appBlocked ->
                Log.d(TAG, "Package Name -> ${appBlocked.packageName}, Application Name -> ${appBlocked.appName}") }
            // Enable Foreground Monitoring
            enableAppForegroundMonitoring()

        } else {
            synchronizeInstalledPackagesInteract(UseCase.None()){
                it.either(::onSyncPackagesFailed, ::onSyncPackagesSuccess)
            }

        }

    }

    /**
     * Enable App Foreground Monitoring
     */
    fun enableAppForegroundMonitoring(){
        Log.d(TAG, "Enable App Foreground Monitoring")

        checkForegroundAppTimer = Timer()
        checkForegroundAppTimer.scheduleAtFixedRate(object: TimerTask(){

            override fun run() {

                // Check Usage Stats Allowed
                if(usageStatsService.isUsageStatsAllowed()) {
                    // Get Current Foreground App
                    val currentAppForeground = usageStatsService.getCurrentForegroundApp()
                    Log.d(TAG, "Current App Foreground -> $currentAppForeground")
                    if(!currentAppForeground.isNullOrEmpty() && appBlockList.isNotEmpty()) {
                        if(appBlockList.map { it.packageName }.contains(currentAppForeground)){
                            Log.d(TAG, "Package $currentAppForeground not allowed")
                        }
                        navigator.showLockScreen(applicationContext)
                    }
                } else {
                    Log.d(TAG, "Usage Stats Not Allowed generate alert")
                }
            }

        }, 0, CHECK_FOREGROUND_APP_INTERVAL)

    }

    /**
     * Disable App Foreground Monitoring
     */
    fun disableAppForegroundMonitoring(){
        Log.d(TAG, "Disable App Foreground Monitoring")
        checkForegroundAppTimer.cancel()
        checkForegroundAppTimer.purge()
    }


    /**
     * Screen Screen Receiver
     */
    inner class ScreenStatusReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {

            if (intent != null && intent.action != null) {

                when {

                    intent.action == Intent.ACTION_SCREEN_ON ||
                    intent.action == Intent.ACTION_SCREEN_OFF ->
                        this@MonitoringService.disableAppForegroundMonitoring()

                    // Screen is unlocked
                    intent.action == Intent.ACTION_USER_PRESENT ->
                        this@MonitoringService.enableAppForegroundMonitoring()

                }
            }
        }
    }

}