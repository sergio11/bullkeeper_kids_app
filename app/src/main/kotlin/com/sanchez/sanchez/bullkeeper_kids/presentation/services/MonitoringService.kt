package com.sanchez.sanchez.bullkeeper_kids.presentation.services

import android.Manifest
import android.annotation.SuppressLint
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
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SystemPackageInfo
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AppStatusChangedReceiver
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import java.util.*
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.location.Location
import android.os.*
import android.support.v4.content.ContextCompat
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AwakenMonitoringServiceBroadcastReceiver
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.here.oksse.ServerSentEvent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import com.here.oksse.OkSse
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SynchronizeTerminalCallHistoryInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts.SynchronizeTerminalContactsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.monitoring.NotifyHeartBeatInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.monitoring.SaveCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.*
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.phonenumber.GetBlockedPhoneNumbersInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.scheduledblocks.SynchronizeScheduledBlocksInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms.SynchronizeTerminalSMSInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository

/**
 * @author Sergio Sánchez Sánchez
 *
 */
class MonitoringService : Service(), ServerSentEvent.Listener {

    // Extra Started From Notification
    private val EXTRA_STARTED_FROM_NOTIFICATION = "STARTED_FROM_NOTIFICATION"

    private val TAG = MonitoringService::class.java.simpleName

    private val NOTIFICATION_ID = 6669999

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
     * Heart Beat Notification Interval
     */
    private val HEARTBEAT_NOTIFICATION_INTERVAL: Long = 10000 // Every minute


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
     * Notify Heart Beat
     */
    private lateinit var notifyHeartBeatTask: Runnable

    /**
     * Dependencies
     */

    /**
     * Synchronize Terminal Call History Interact
     */
    @Inject
    internal lateinit var synchronizeTerminalCallHistoryInteract: SynchronizeTerminalCallHistoryInteract

    /**
     * Synchronize Terminal Contacts Interact
     */
    @Inject
    internal lateinit var synchronizeTerminalContactsInteract: SynchronizeTerminalContactsInteract

    /**
     * Synchronize Terminal SMS Interact
     */
    @Inject
    internal lateinit var synchronizeTerminalSMSInteract: SynchronizeTerminalSMSInteract

    /**
     * Synchronize Scheduled Blocks Interact
     */
    @Inject
    internal lateinit var synchronizeScheduledBlocksInteract: SynchronizeScheduledBlocksInteract


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
    internal lateinit var getAllAppsInstalledInteract: GetAllAppsInstalledInteract

    /**
     * Usage Stats Service
     */
    @Inject
    internal lateinit var usageStatsService: IUsageStatsService

    /**
     * Navigator
     */
    @Inject
    internal lateinit var navigator: INavigator


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
     * Api End Points Helper
     */
    @Inject
    internal lateinit var apiEndPointsHelper: ApiEndPointsHelper

    /**
     * OK SSE Client
     */
    @Inject
    internal lateinit var okSse: OkSse

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Notify Heart Beat Interact
     */
    @Inject
    internal lateinit var notifyHeartBeatInteract: NotifyHeartBeatInteract

    /**
     * Save Current Location Interact
     */
    @Inject
    internal lateinit var saveCurrentLocationInteract: SaveCurrentLocationInteract

    /**
     * Save App Installed Interact
     */
    @Inject
    internal lateinit var saveAppInstalledInteract: SaveAppInstalledInteract

    /**
     * Remove Installed Package Interact
     */
    @Inject
    internal lateinit var removeInstalledPackageInteract: RemoveInstalledPackageInteract

    /**
     * Get Blocked Phone Numbers Interact
     */
    @Inject
    internal lateinit var getBlockedPhoneNumbersInteract: GetBlockedPhoneNumbersInteract


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
     * Fused Location Provider Service
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * State
     */
    private var appBlockList: Set<SystemPackageInfo> = HashSet()

    private var currentAppLocked: String? = null

    private var serverSentEvent: ServerSentEvent? = null




    /**
     * Get Notification
     */
    private fun getNotification(): Notification {

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
        disableHeartBeatMonitoring()
        stopListenSse()
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

        // Start Services
        startBasicServices()

        // Start Data Synchronization
        startDataSynchronization()

        // Start service with notification
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
    private fun injectDependencies() {
        serviceComponent.inject(this)
    }

    /**
     * Start Basic Services
     */
    private fun startBasicServices(){
        // Launch SSE Listener
        startListenSse()
        // Enable HeartBeat Monitoring
        enableHeartBeatMonitoring()
        // Start Location Tracking
        startLocationTracking()
        // Enable App Foreground Monitoring
        enableAppForegroundMonitoring()
    }

    /**
     * Start Data Synchronization
     */
    private fun startDataSynchronization(){
        syncPhoneNumbersBlocked()
        syncTerminalApps()
        syncTerminalCallHistory()
        syncTerminalContacts()
        syncTerminalSMS()
        syncScheduledBlocks()
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
                            //navigator.showLockScreen(this)
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
                    it.either(fnL = fun(failure) {
                        Timber.d("Sync Usage Stats Failed")
                    } , fnR = fun(count: Int){
                        Timber.d("Sync Usage stats success, count -> $count")
                    })
                }
            }

            mHandler.postDelayed(checkApplicationsUsageStatistics, CHECK_APPLICATIONS_USAGE_STATISTICS)
        }


        /**
         * Notify Heart Beat Task
         */

        notifyHeartBeatTask = Runnable {

            notifyHeartBeatInteract(UseCase.None()){
                it.either(fnL = fun(failure) {
                    Timber.d( "Heartbeat notification failed")
                } , fnR = fun(result){
                    Timber.d("HeartBeat Notified -> $result")
                })
            }

            mHandler.postDelayed(notifyHeartBeatTask, HEARTBEAT_NOTIFICATION_INTERVAL)

        }

    }


    /**
     * Enable App Foreground Monitoring
     */
    private fun enableAppForegroundMonitoring(){
        Log.d(TAG, "Enable App Foreground Monitoring")
        mHandler.postDelayed(checkForegroundAppTask, CHECK_FOREGROUND_APP_INTERVAL)
        mHandler.postDelayed(checkApplicationsUsageStatistics, CHECK_APPLICATIONS_USAGE_STATISTICS)
    }

    /**
     * Disable App Foreground Monitoring
     */
    private fun disableAppForegroundMonitoring(){
        Log.d(TAG, "Disable App Foreground Monitoring")
        mHandler.removeCallbacks(checkForegroundAppTask)
        mHandler.removeCallbacks(checkApplicationsUsageStatistics)
    }

    /**
     * Enable Heart Beat Monitoring
     */
    private fun enableHeartBeatMonitoring(){
        Log.d(TAG, "Enable Heart Beat Monitoring")
        mHandler.postDelayed(notifyHeartBeatTask, HEARTBEAT_NOTIFICATION_INTERVAL)
    }

    /**
     * Disable Heart Beat Monitoring
     */
    private fun disableHeartBeatMonitoring(){
        Log.d(TAG, "Disable Heart Beat Monitoring")
        mHandler.removeCallbacks(notifyHeartBeatTask)
    }

    /**
     * Notify Current Location
     */
    private fun notifyCurrentLocation(location: Location) {
        saveCurrentLocationInteract(SaveCurrentLocationInteract.Params(
                latitude = location.latitude,
                longitude = location.longitude
        )){
            it.either(fnL = fun(failure){
                Timber.d("Notification of the current position failed")
            }, fnR = fun(location){
                Timber.d("Current position successfully notified")
            })
        }
    }


    /**
     * Syn Phone Numbers Blocked
     */
    private fun syncPhoneNumbersBlocked() {
        getBlockedPhoneNumbersInteract(UseCase.None()){
            it.either(fnL = fun(_: Failure){
                Timber.d("Get Phone Numbers Blocked Failed")
            }, fnR = fun(_: Unit){
                Timber.d("Phone Numbers Blocked Saved Successfully")
            })
        }
    }

    /**
     * Sync Terminal Apps
     */
    private fun syncTerminalApps(){
        synchronizeInstalledPackagesInteract(UseCase.None()){
            it.either(fnL = fun(_:Failure){
                Timber.d("Apps Sync Failed")
            }, fnR = fun(total: Int){
                Timber.d("Apps Sync successfully, Total ->  %d", total)
            })
        }

    }

    /**
     * Sync Terminal Call History
     */
    private fun syncTerminalCallHistory(){
        synchronizeTerminalCallHistoryInteract(UseCase.None()){
            it.either(fnL = fun(_: Failure){
                Timber.d("Sync Terminal Call History failed")
            }, fnR = fun(total: Int){
                Timber.d("Sync Terminal Call History Success, Total -> %d", total)
            })
        }

    }

    /**
     * Sync Terminal Contacts
     */
    private fun syncTerminalContacts(){
        synchronizeTerminalContactsInteract(UseCase.None()){
            it.either(fnL = fun(_: Failure){
                Timber.d("Sync Terminal Contacts failed")
            }, fnR = fun(totalContacts: Int){
                Timber.d("Sync Terminal Contacts Success -> Total %d", totalContacts)
            })
        }
    }

    /**
     * Sync Terminal SMS
     */
    private fun syncTerminalSMS(){
        synchronizeTerminalSMSInteract(UseCase.None()){
            it.either(fnL = fun(_: Failure){
                Timber.d("Sync Terminal SMS failed")
            }, fnR = fun(totalSms: Int){
                Timber.d("Sync Terminal Sms Success -> Total %d", totalSms)
            })
        }
    }

    /**
     * Sync Scheduled Blocks
     */
    private fun syncScheduledBlocks(){
        synchronizeScheduledBlocksInteract(UseCase.None()){
            it.either(fnL = fun(_: Failure){
                Timber.d("Sync Scheduled Blocks Failed")
            }, fnR = fun(_: Unit){
                Timber.d("Sync Scheduled Blocks successfully")
            })
        }
    }


    /**
     * Start Location Tracking
     */
    @SuppressLint("MissingPermission")
    private fun startLocationTracking() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Last Location
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    location?.let { notifyCurrentLocation(it) }
                }


        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 120000 // two minute interval
        mLocationRequest.fastestInterval = 120000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                fusedLocationClient.requestLocationUpdates(mLocationRequest,
                        object: LocationCallback(){
                            override fun onLocationResult(locationResult: LocationResult?) {
                                locationResult?.let {
                                    if(!it.locations.isNullOrEmpty()) {
                                        notifyCurrentLocation(it.locations[it.locations.size - 1])
                                    }
                                }
                            }
                        },
                        Looper.myLooper())
            } else {
                //Request Location Permission
            }
        }
        else {
            fusedLocationClient.requestLocationUpdates(mLocationRequest,
                    object: LocationCallback(){
                        override fun onLocationResult(locationResult: LocationResult?) {
                            locationResult?.let {
                                if(!it.locations.isNullOrEmpty()) {
                                    notifyCurrentLocation(it.locations[it.locations.size - 1])
                                }
                            }
                        }
                    },
                    Looper.myLooper())
        }

    }

    /**
     * Server Sent Events
     */

    /**
     * Start Listen SSE
     */
    private fun startListenSse() {

        serverSentEvent?.close()
        serverSentEvent = null

        if (preferenceRepository.getPrefCurrentUserIdentity()
                != IPreferenceRepository.CURRENT_USER_IDENTITY_DEFAULT_VALUE) {

            Timber.d("Start Listen SSE")
            val eventSubscriptionRequest = Request.Builder()
                    .url(apiEndPointsHelper.getEventSubscriptionUrl(preferenceRepository
                            .getPrefCurrentUserIdentity()))
                    .build()
            serverSentEvent = okSse.newServerSentEvent(eventSubscriptionRequest, this)
        }
    }

    /**
     * Stop Listen Sse
     */
    private fun stopListenSse() {
        serverSentEvent?.close()
        serverSentEvent = null
    }

    /**
     * On Open
     */
    override fun onOpen(sse: ServerSentEvent?, response: Response?) {
        Timber.d("SSE: On Open")
    }

    /**
     * On Retry Time
     */
    override fun onRetryTime(sse: ServerSentEvent?, milliseconds: Long): Boolean {
        Timber.d("SSE: On Retry Time")
        return false
    }

    /**
     * On Comment
     */
    override fun onComment(sse: ServerSentEvent?, comment: String?) {
        Timber.d("ServerSentEventHandler: On Comment -> %s", comment)
    }

    /**
     * On Retry Error
     */
    override fun onRetryError(sse: ServerSentEvent?, throwable: Throwable?, response: Response?): Boolean {
        Timber.d("SSE: On Retry Error")
        return false
    }

    /**
     * On Pre Retry
     */
    override fun onPreRetry(sse: ServerSentEvent?, originalRequest: Request?): Request? {
        Timber.d("SSE: On Pre Retry")
        return null
    }

    /**
     * On Message
     */
    override fun onMessage(sse: ServerSentEvent?, id: String?, event: String?, message: String?) {
        Timber.d("ServerSentEventHandler: On Message -> %s", message)
    }

    /**
     * On Closed
     */
    override fun onClosed(sse: ServerSentEvent?) {
        Timber.d("ServerSentEventHandler: On Closed")
        startListenSse()
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