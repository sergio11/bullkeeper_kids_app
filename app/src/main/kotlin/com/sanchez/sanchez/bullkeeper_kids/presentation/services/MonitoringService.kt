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
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Binder
import android.os.IBinder
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AwakenMonitoringServiceBroadcastReceiver
import android.support.v4.content.LocalBroadcastManager
import android.os.Handler
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SynPackageUsageStatsInteract


/**
 * @author Sergio Sánchez Sánchez
 *
 */
class MonitoringService : Service(){

    // Extra Started From Notification
    private val EXTRA_STARTED_FROM_NOTIFICATION = "STARTED_FROM_NOTIFICATION"

    private val TAG = MonitoringService::class.java.simpleName

    private val NOTIFICATION_ID = 6669999;

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
     * Check Applications Usage Statistics Interval
     */
    private val CHECK_APPLICATIONS_USAGE_STATISTICS: Long = 300000

    /**
     * Task Handler
     */
    private var mHandler: Handler = Handler()

    /**
     * Check Foreground App Task
     */
    private lateinit var checkForegroundAppTask: Runnable

    /**
     * Check Applications Usage Statistics
     */
    private lateinit var checkApplicationsUsageStatistics : Runnable

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
     * Sync Usage Stats Interact
     */
    @Inject
    internal lateinit var syncUsageStatsInteract: SynPackageUsageStatsInteract


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

    private var currentAppLocked: String? = null;


    /**
     * Get Notification
     */
    fun getNotification(): Notification {

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
     * Clean Resources
     */
    fun cleanResources() {
        unregisterReceiver(appStatusChangedReceiver)
        unregisterReceiver(screenStatusReceiver)
        disableAppForegroundMonitoring()
    }


    /**
     * On Create
     */
    override fun onCreate() {
        super.onCreate()
        // Inject Dependencies
        injectDependencies()

        Log.d(TAG, "On Create Monitoring Service")
        Thread.setDefaultUncaughtExceptionHandler(MonitoringServiceUncaughtExceptionHandler(this))

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

        // Init Task
        initTask()

        // Get All Packages Installed
        getAllPackagesInstalledInteract(UseCase.None()){
            it.either(::onGetAllPackagesInstalledFailed, ::onGetAllPackagesInstalledSuccess)
        }

        startForeground(NOTIFICATION_ID, getNotification())

    }

    /**
     * On Destroy
     */
    override fun onDestroy() {
        Log.d(TAG, "On Destroy Monitoring Service")
        cleanResources()
        sendBroadcast(Intent(this,
                AwakenMonitoringServiceBroadcastReceiver::class.java))
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
    fun injectDependencies() {
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
        enableAppForegroundMonitoring()

    }


    /**
     * On Sync Package Usage Stats Failed
     */
    private fun onSyncPackageUsageStatsFailed(failure: Failure) {
        Log.d(TAG, "On Sync Package Usage Stats")

    }

    /**
     * On Sync Package Usage Stats Success
     */
    private fun onSyncPackageUsageStatsSuccess(syncPackages: Int) {
        Log.d(TAG, "Sync Packages count -> $syncPackages")
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
     * Init Task
     */
    private fun initTask(){

        /**
         * Init Check Foreground App Task
         */
        checkForegroundAppTask = Runnable {

            Log.d(TAG, "Check Foreground App Task")

            if (appBlockList.isNotEmpty()) {
                // Check Usage Stats Allowed
                if (usageStatsService.isUsageStatsAllowed()) {
                    // Get Current Foreground App
                    val currentAppForeground = usageStatsService.getCurrentForegroundApp()
                    Log.d(TAG, "Current App Foreground -> $currentAppForeground")
                    if (!currentAppForeground.isNullOrEmpty() &&
                            currentAppForeground != packageName &&
                            currentAppForeground != currentAppLocked) {

                        if (appBlockList.map { it.packageName }.contains(currentAppForeground)) {
                            Log.d(TAG, "Package $currentAppForeground not allowed")
                            currentAppLocked = currentAppForeground
                            navigator.showLockScreen(applicationContext)
                        } else {
                            currentAppLocked = null
                            val localBroadcastManager = LocalBroadcastManager
                                    .getInstance(this@MonitoringService)
                            localBroadcastManager.sendBroadcast(Intent(
                                    "com.sanchez.sergio.unlock"))
                        }
                    }

                } else {
                    Log.d(TAG, "Usage Stats Not Allowed generate alert")
                }
            }

            mHandler.postDelayed(checkForegroundAppTask, CHECK_FOREGROUND_APP_INTERVAL)
        }

        /**
         * Check Applications Usage Statistics
         */
        checkApplicationsUsageStatistics = Runnable {

            Log.d(TAG, "Check Applications Usage Statistics")

            // Check Usage Stats Allowed
            if (usageStatsService.isUsageStatsAllowed()) {
                // Sync Usage Stats
                syncUsageStatsInteract(UseCase.None()){
                    it.either(::onSyncPackageUsageStatsFailed,
                            ::onSyncPackageUsageStatsSuccess)
                }
            }

            mHandler.postDelayed(checkApplicationsUsageStatistics, CHECK_APPLICATIONS_USAGE_STATISTICS)
        }

    }


    /**
     * Enable App Foreground Monitoring
     */
    fun enableAppForegroundMonitoring(){
        Log.d(TAG, "Enable App Foreground Monitoring")
        mHandler.postDelayed(checkForegroundAppTask, CHECK_FOREGROUND_APP_INTERVAL)
        mHandler.postDelayed(checkApplicationsUsageStatistics, CHECK_APPLICATIONS_USAGE_STATISTICS)
    }

    /**
     * Disable App Foreground Monitoring
     */
    fun disableAppForegroundMonitoring(){
        Log.d(TAG, "Disable App Foreground Monitoring")
        mHandler.removeCallbacks(checkForegroundAppTask)
        mHandler.removeCallbacks(checkApplicationsUsageStatistics)
    }


    /**
     * Monitoring Service
     */
    inner class MonitoringServiceUncaughtExceptionHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(t: Thread, e: Throwable) {
            cleanResources()
            val i = Intent(context, AwakenMonitoringServiceBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0)
            val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)
            System.exit(2)
        }
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


    override fun onBind(intent: Intent?): IBinder  = Binder()
}