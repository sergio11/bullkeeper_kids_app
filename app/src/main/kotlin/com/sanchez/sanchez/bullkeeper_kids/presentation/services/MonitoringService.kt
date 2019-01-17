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
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import javax.inject.Inject
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AppStatusChangedReceiver
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.location.Location
import android.os.*
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.AwakenMonitoringServiceBroadcastReceiver
import com.google.android.gms.location.*
import com.here.oksse.ServerSentEvent
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import com.here.oksse.OkSse
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppInstalledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppRuleEnum
import com.sanchez.sanchez.bullkeeper_kids.data.entity.FunTimeDayScheduledEntity
import com.sanchez.sanchez.bullkeeper_kids.data.entity.ScheduledBlockEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.models.response.FunTimeScheduledDTO
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppAllowedByScheduledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IFunTimeDayScheduledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.ScheduledBlocksRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.data.sse.*
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SynchronizeTerminalCallHistoryInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts.SynchronizeTerminalContactsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.funtime.SyncFunTimeInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.SaveGeofenceInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.RemoveGeofenceInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.geofences.SyncGeofencesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.monitoring.NotifyHeartBeatInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.monitoring.SaveCurrentLocationInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.*
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.phonenumber.GetBlockedPhoneNumbersInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.scheduledblocks.SynchronizeScheduledBlocksInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms.SynchronizeTerminalSMSInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.UnlinkTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.ServerEventTypeEnum
import com.sanchez.sanchez.bullkeeper_kids.domain.observers.ContactsObserver
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.bedtime.BedTimeActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen.DisabledAppScreenActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen.AppLockScreenActivity
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

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
     * Bed Time Const
     */

    private val START_BED_TIME_HOUR = 23
    private val END_BED_TIME_HOUR = 7

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
    private val CHECK_APPLICATIONS_USAGE_STATISTICS: Long = 30000

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
     * Synchronize Installed Packages Interact
     */
    @Inject
    internal lateinit var synchronizeInstalledPackagesInteract:
            SynchronizeInstalledPackagesInteract


    /**
     * Local Notification Service
     */
    @Inject
    internal lateinit var localNotificationService: ILocalNotificationService



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
     * Sync Usage Stats Interact
     */
    @Inject
    internal lateinit var syncUsageStatsInteract: SyncPackageUsageStatsInteract

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
     * Apps Installed Repository
     */
    @Inject
    internal lateinit var appsInstalledRepository: IAppsInstalledRepository

    /**
     * Scheduled Block Repository
     */
    @Inject
    internal lateinit var scheduledBlocksRepositoryImpl: ScheduledBlocksRepositoryImpl

    /**
     * Unlink Terminal
     */
    @Inject
    internal lateinit var unlinkTerminalInteract: UnlinkTerminalInteract

    /**
     * Object Mapper
     */
    @Inject
    internal lateinit var objectMapper: ObjectMapper

    /**
     * Contacts Observer
     */
    @Inject
    internal lateinit var contactsObserver: ContactsObserver

    /**
     * Sync Geofences Interact
     */
    @Inject
    internal lateinit var syncGeofencesInteract: SyncGeofencesInteract

    /**
     * Add Geofence Interact
     */
    @Inject
    internal lateinit var saveGeofenceInteract: SaveGeofenceInteract

    /**
     * Remove Geofence Interact
     */
    @Inject
    internal lateinit var removeGeofenceInteract: RemoveGeofenceInteract

    /**
     * Sync Fun Time Interact
     */
    @Inject
    internal lateinit var syncFunTimeInteract: SyncFunTimeInteract

    /**
     * Fun Time Day Scheduled Repository
     */
    @Inject
    internal lateinit var funTimeDayScheduledRepository: IFunTimeDayScheduledRepository

    /**
     * Apps Allowed By Scheduled Repository
     */
    @Inject
    internal lateinit var appsAllowedByScheduledRepository: IAppAllowedByScheduledRepository


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
     *
     */

    // Current App Locked
    private var currentAppLocked: String? = null

    // Current Disabled App
    private var currentDisabledApp: String? = null

    // Is Bed Time Enabled
    private var isBedTimeEnabled: Boolean = false

    // Server Sent Event
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
    private fun cleanResources() {
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
        if(preferenceRepository.getPrefKidIdentity()
                != IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE &&
                preferenceRepository.getPrefTerminalIdentity() !=
                    IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE)
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
        // Tells the system try  recreate the service after it has been killed.
        return Service.START_STICKY
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
        syncGeofences()
        syncFunTime()
        startContactObserver()
    }

    /**
     * Start Contact Observer
     */
    private fun startContactObserver(){
        try{
            //Registering contact observer
            application.contentResolver
                    .registerContentObserver(ContactsContract.Contacts.CONTENT_URI,
                            true, contactsObserver)
            Timber.d("Contact Observer registered")
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    /**
     * Send Unlock App Action
     */
    private fun sendUnLockAppAction(){
        if(!currentAppLocked.isNullOrEmpty()) {
            currentAppLocked = null
            val localBroadcastManager = LocalBroadcastManager
                    .getInstance(this@MonitoringService)
            localBroadcastManager.sendBroadcast(Intent(
                    AppLockScreenActivity.UNLOCK_APP_ACTION))
        }
    }

    /**
     * Send Enable App Action
     */
    private fun sendEnableAppAction(){
        if(!currentDisabledApp.isNullOrEmpty()) {
            currentDisabledApp = null
            val localBroadcastManager = LocalBroadcastManager
                    .getInstance(this@MonitoringService)
            localBroadcastManager.sendBroadcast(Intent(
                    DisabledAppScreenActivity.ENABLE_APP_ACTION))
        }
    }

    /**
     * Send Disable Bed Time Action
     */
    private fun sendDisableBedTimeAction(){
        if(isBedTimeEnabled) {
            isBedTimeEnabled = false
            val localBroadcastManager = LocalBroadcastManager
                    .getInstance(this@MonitoringService)
            localBroadcastManager.sendBroadcast(Intent(
                    BedTimeActivity.DISABLE_BED_TIME_ACTION))
        }
    }

    /**
     * Lock Current App
     */
    private fun lockCurrentApp(currentAppForeground: String,
                               appInstalled: AppInstalledEntity,
                               appLockType: AppLockScreenActivity.Companion.LockTypeEnum) {
        Timber.d("CFAT: Lock Current Foreground app: %s", currentAppForeground)
        if(currentAppLocked.isNullOrEmpty() || currentAppForeground == appInstalled.packageName) {
            currentAppLocked = appInstalled.packageName
            navigator.showLockScreen(this, appLockType, appInstalled.packageName,
                    appInstalled.appName, appInstalled.icon, appInstalled.appRule)
        }
    }

    /**
     * Lock By Scheduled Block Enabled
     */
    private fun lockByScheduledBlockEnabled(currentAppForeground: String,
                                            appInstalled: AppInstalledEntity,
                                            scheduledBlockEntity: ScheduledBlockEntity) {

        Timber.d("CFAT: Lock Current Foreground app: %s", currentAppForeground)
        if(currentAppLocked.isNullOrEmpty() || currentAppForeground == appInstalled.packageName) {
            currentAppLocked = appInstalled.packageName
            navigator.showScheduledBlockActive(this, scheduledBlockEntity.name,
                    scheduledBlockEntity.image, scheduledBlockEntity.startAt, scheduledBlockEntity.endAt,
                    scheduledBlockEntity.description)
        }
    }


    /**
     * App Disabled Staten Handler
     */
    private fun appDisabledStateHandler(
            currentAppForeground: String,
            appInstalled: AppInstalledEntity) {
        sendUnLockAppAction()
        Timber.d("CFAT: Disabled Current Foreground app: %s", appInstalled.packageName)
        if(currentDisabledApp.isNullOrEmpty() || currentAppForeground == appInstalled.packageName) {
            currentDisabledApp = appInstalled.packageName
            navigator.showDisabledAppScreen(this, appInstalled.packageName,
                    appInstalled.appName, appInstalled.icon, appInstalled.appRule)
        }
    }

    /**
     * Fun Time App Rule Handler
     */
    private fun funTimeAppRuleHandler(currentAppForeground: String, appInstalled: AppInstalledEntity) {

        val currentScheduledBlockEnable =
                scheduledBlocksRepositoryImpl.getScheduledBlockEnableForThisMoment(
                        getString(R.string.joda_local_time_format_server_response))

        Timber.d("CFAT: FUN_TIME rule applied to the current application ")

        if(currentScheduledBlockEnable != null) {
            // Fun time not available with an active "Scheduled Block"
            lockByScheduledBlockEnabled(currentAppForeground,
                    appInstalled, currentScheduledBlockEnable)
        } else {
            // Check Fun Time is enabled
            if(preferenceRepository.isFunTimeEnabled()) {
                val format = SimpleDateFormat("EEEE", Locale.US)
                val dayOfWeek = format.format(Calendar.getInstance().time)
                        .toUpperCase()
                funTimeDayScheduledRepository.getFunTimeDayScheduledForDay(dayOfWeek)?.let {

                    if(!it.day.isNullOrEmpty() && it.enabled) {

                        // Current Day Scheduled
                        val currentDayScheduled = preferenceRepository
                                .getCurrentFunTimeDayScheduled()
                        // Check Current Day Scheduled
                        if(currentDayScheduled != it.day) {
                            preferenceRepository.setCurrentFunTimeDayScheduled(it.day!!)
                            // Save Remaining Fun Time in seconds
                            preferenceRepository.setRemainingFunTime(it.totalHours * 60 * 60)
                        }

                        // Get Remaining Fun Time
                        var remainingFunTimeSaved = preferenceRepository.getRemainingFunTime()

                        if(remainingFunTimeSaved <= 0) {
                            // Fun Time exhausted
                            lockCurrentApp(currentAppForeground, appInstalled,
                                    AppLockScreenActivity.Companion.LockTypeEnum.FUN_TIME_UNAVAILABLE)
                        } else {
                            //
                            preferenceRepository.setRemainingFunTime(remainingFunTimeSaved -
                                    (CHECK_FOREGROUND_APP_INTERVAL / 1000))
                        }

                    } else {
                        // Fun Time No Avaliable
                        lockCurrentApp(currentAppForeground, appInstalled,
                                AppLockScreenActivity.Companion.LockTypeEnum.FUN_TIME_UNAVAILABLE)
                    }
                }
            } else {
                // Fun Time Disabled
                lockCurrentApp(currentAppForeground, appInstalled,
                        AppLockScreenActivity.Companion.LockTypeEnum.FUN_TIME_DISABLED)
            }
        }
    }

    /**
     * Per Scheduled App Rule Handler
     */
    private fun perScheduledAppRuleHandler(currentAppForeground: String, appInstalled: AppInstalledEntity) {
        Timber.d("CFAT: PER_SCHEDULER rule applied to the current application ")

        val currentScheduledBlockEnable =
                scheduledBlocksRepositoryImpl.getScheduledBlockEnableForThisMoment(
                        getString(R.string.joda_local_time_format_server_response))

        if(currentScheduledBlockEnable != null) {

            if(appsAllowedByScheduledRepository.anyAppAllowed(
                    scheduledBlock = currentScheduledBlockEnable.id!!
            ) && !appsAllowedByScheduledRepository.isAppAllowed(
                            app = appInstalled.packageName!!,
                            scheduledBlock = currentScheduledBlockEnable.id!!
                    )) {

                lockByScheduledBlockEnabled(currentAppForeground,
                        appInstalled, currentScheduledBlockEnable)

            } else {
                sendUnLockAppAction()
            }

        } else {
            // No Scheduled Block Enable
            lockCurrentApp(currentAppForeground, appInstalled,
                    AppLockScreenActivity.Companion.LockTypeEnum.NO_SCHEDULED_BLOCK_ENABLE)
        }
    }

    /**
     * Always Allowed App Rule Handler
     */
    private fun alwaysAllowedAppRuleHandler(currentAppForeground: String, appInstalled: AppInstalledEntity){
        Timber.d("CFAT: ALWAYS_ALLOWED rule applied to the current application ")
        sendUnLockAppAction()
    }

    /**
     * Never Allowed App Rule Handler
     */
    private fun neverAllowedAppRuleHandler(currentAppForeground: String, appInstalled: AppInstalledEntity){
        Timber.d("CFAT: NEVER_ALLOWED rule applied to the current application ")
        lockCurrentApp(currentAppForeground, appInstalled,
                AppLockScreenActivity.Companion.LockTypeEnum.APP_NOT_ALLOWED)
    }

    /**
     * Init Task
     */
    private fun initTask(){


        /**
         * Init Check Foreground App Task
         */
        checkForegroundAppTask = Runnable {

            Timber.d("Check Foreground App Task")

            // Check Usage Stats Allowed
            if (usageStatsService.isUsageStatsAllowed()) {
                // Get Current Foreground App
                val currentAppForeground = usageStatsService.getCurrentForegroundApp()
                Timber.d("CFAT: Current App Foreground -> %s", currentAppForeground)
                if (!currentAppForeground.isNullOrEmpty()
                        && (!currentAppLocked.isNullOrEmpty() ||
                                !currentDisabledApp.isNullOrEmpty() ||
                                currentAppForeground != packageName)){

                    val startBedTime = LocalTime(START_BED_TIME_HOUR, 0)
                    val endBedTime = LocalTime(END_BED_TIME_HOUR, 0)
                    val currentTime = LocalTime.now()

                    if(currentTime.isAfter(startBedTime)
                            && currentTime.isBefore(endBedTime)) {
                        Timber.d("CFAT: Bet Time active")
                        sendUnLockAppAction()
                        sendEnableAppAction()
                        if(preferenceRepository.isBedTimeEnabled()) {
                            Timber.d("CFAT: Bet Time enabled")
                            isBedTimeEnabled = true
                            navigator.showBedTimeScreen(this)
                        } else {
                            Timber.d("CFAT: Bet Time disabled")
                            // Disable Bed Time is needed
                            sendDisableBedTimeAction()
                        }
                    } else {

                        // Disable Bed Time is needed
                        sendDisableBedTimeAction()

                        // Get information about the application in the BD
                        Timber.d("CFAT: Current App in foreground -> %s ", currentAppForeground)

                        // Get App To Check
                        val appToCheck: String = when {
                            !currentAppLocked.isNullOrEmpty()
                                    && currentAppForeground == packageName -> currentAppLocked!!
                            !currentDisabledApp.isNullOrEmpty()
                                    && currentAppForeground == packageName -> currentDisabledApp!!
                            else -> currentAppForeground
                        }

                        // Get Last Data from app
                        val appInstalledEntity =
                                appsInstalledRepository.findByPackageName(appToCheck)

                        appInstalledEntity?.let {appInstalled->

                            if(appInstalled.disabled) {
                                appDisabledStateHandler(currentAppForeground, appInstalled)

                            } else {

                                sendEnableAppAction()

                                if(appInstalled.appRule != null) {

                                    try {

                                        val appRuleEnum = AppRuleEnum.valueOf(appInstalled.appRule!!)

                                        when(appRuleEnum) {
                                            AppRuleEnum.PER_SCHEDULER -> perScheduledAppRuleHandler(currentAppForeground, appInstalled)
                                            AppRuleEnum.FUN_TIME -> funTimeAppRuleHandler(currentAppForeground, appInstalled)
                                            AppRuleEnum.ALWAYS_ALLOWED -> alwaysAllowedAppRuleHandler(currentAppForeground, appInstalled)
                                            AppRuleEnum.NEVER_ALLOWED -> neverAllowedAppRuleHandler(currentAppForeground, appInstalled)
                                        }

                                    } catch (ex: Exception) {
                                        Timber.d("CFAT: ex: %s", ex.message)
                                        ex.printStackTrace()
                                    }

                                }

                            }
                        }

                    }

                }


            } else {
                Log.d(TAG, "Usage Stats Not Allowed generate alert")
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
     * Save Current Location
     */
    private fun saveCurrentLocation(location: Location) {
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
     * Sync Geofences
     */
    private fun syncGeofences(){
        syncGeofencesInteract(UseCase.None()){
            it.either(fnL = fun(_: Failure){
                Timber.d("Sync Geofences Failed")
            }, fnR = fun(total: Int){
                Timber.d("Sync Geofences successfully, total -> %d", total)
            })
        }
    }


    /**
     * Sync Fun Time
     */
    private fun syncFunTime(){
        syncFunTimeInteract(UseCase.None()){
            it.either(fnL = fun(_: Failure){
                Timber.d("Sync Fun Time Failed")
            }, fnR = fun(_: Unit){
                Timber.d("Sync Fun Time successfully")
            })
        }
    }

    /**
     * Unlink Terminal
     */
    private fun unlinkTerminal(){
        Timber.d("Unlink Terminal")
        unlinkTerminalInteract(UseCase.None()){
            it.either(fnL = fun(_: Failure){
                Timber.d("Unlink Terminal Failed")
            }, fnR = fun(_){
                Timber.d("Unlink Terminal Success")
                navigator.showLogin(this)
                stopSelf()
            })
        }

    }

    /**
     * Request Location Updates
     */
    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(locationRequest: LocationRequest){

        fusedLocationClient.requestLocationUpdates(locationRequest,
                object: LocationCallback(){
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult?.let {
                            if(!it.locations.isNullOrEmpty()) {
                                saveCurrentLocation(it.locations[it.locations.size - 1])
                            }
                        }
                    }
                },
                Looper.myLooper())
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
                    location?.let {
                        saveCurrentLocation(it) }
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
                requestLocationUpdates(mLocationRequest)
            } else {
                //Request Location Permission
            }
        }
        else {
            requestLocationUpdates(mLocationRequest)
        }

    }

    /**
     * Server Sent Events
     */

    /**
     * Delete All Scheduled Block Event Handler
     */
    private fun deleteAllScheduledBlockEventHandler(event: DeleteAllScheduledBlockDTO){
        Timber.d("SSE: Delete All Scheduled Block Event Handler")
        event.kid?.let { scheduledBlocksRepositoryImpl.deleteByKid(it) }

    }

    /**
     * Delete Scheduled Block Event Handler
     */
    private fun deleteScheduledBlockEventHandler(event: DeleteScheduledBlockDTO){
        Timber.d("SSE: Delete Scheduled Block Event Handler")
        if(event.kid != null && event.block != null)
            scheduledBlocksRepositoryImpl.deleteByKidAndBlock(event.kid!!, event.block!!)
    }

    /**
     * Scheduled Block Image Change Event Handler
     */
    private fun scheduledBlockImageChangeEventHandler(event: ScheduledBlockImageChangedDTO) {
        Timber.d("SSE: Scheduled Block Image Change Event Handler")
        if(event.block != null && event.image != null)
            scheduledBlocksRepositoryImpl.updateImage(event.block!!, event.image!!)
    }

    /**
     * Scheduled Block Saved Event Handler
     */
    private fun scheduledBlockSavedEventHandler(event: ScheduledBlockSavedDTO) {
        Timber.d("SSE: Scheduled Block Saved Event Handler")
        val fmt = DateTimeFormat.forPattern(
                getString(R.string.joda_local_time_format_server_response))
        scheduledBlocksRepositoryImpl.save(ScheduledBlockEntity(
                id = event.identity,
                name = event.name, enable = event.enable,
                repeatable = event.repeatable,
                image = event.image, kid = event.kid,
                startAt = event.startAt?.toString(fmt),
                endAt = event.endAt?.toString(fmt),
                weeklyFrequency = event.weeklyFrequency?.joinToString(",")))
    }

    /**
     * Scheduled Block Status Changed Event
     */
    private fun scheduledBlockStatusChangedEventHandler(event: ScheduledBlockStatusChangedDTO){
        Timber.d("SSE: Scheduled Block Status Changed Event")
        event.scheduledBlockStatusList.forEach {
            if(it.identity != null && it.enable != null)
            scheduledBlocksRepositoryImpl.updateStatus(it.identity!!, it.enable!!)
        }

    }

    /**
     * App Rules List Saved Event Handler
     */
    private fun  appRulesListSavedEventHandler(event: AppRulesListSavedDTO){
        Timber.d("SSE: App Rules List Saved")
        event.appRulesList.forEach {
            if(it.identity != null && it.type != null) {
                Timber.d("SSE: Update App Rules: app: %s, type : %s", it.identity!!, it.type!!)
                appsInstalledRepository.updateAppRule(it.identity!!, it.type!!)
            }
        }
    }

    /**
     * App Rules Saved Event Handler
     */
    private fun appRulesSavedEventHandler(event: AppRulesSavedDTO) {
        Timber.d("SSE: App Rules Saved Event Handler")
        if(event.app != null && event.type != null)
            appsInstalledRepository.updateAppRule(event.app!!, event.type!!)
    }


    /**
     * Bed Time Status Changed Handler
     */
    private fun bedTimeStatusChangedHandler(event: TerminalBedTimeStatusChangedDTO) {
        Timber.d("SSE: Bed Time Status Changed Handler")
        event.enabled?.let { preferenceRepository.setBedTimeEnabled(it) }
    }

    /**
     * Terminal Camera Status Changed Handler
     */
    private fun terminalCameraStatusChangedHandler(event: TerminalCameraStatusChangedDTO) {
        Timber.d("SSE: Terminal Camera Status Changed Handler")
        event.enabled?.let { preferenceRepository.setCameraEnabled(it) }
    }


    /**
     * Terminal Screen Status Changed Handler
     */
    private fun terminalScreenStatusChangedHandler(event: TerminalScreenStatusChangedDTO) {
        Timber.d("SSE: Terminal Screen status Changed Handler")
        event.enabled?.let { preferenceRepository.setLockScreenEnabled(it) }
    }

    /**
     * App Disabled Status Changed Handler
     */
    private fun appDisabledStatusChangedHandler(event: AppDisabledStatusChangedDTO) {
        Timber.d("SSE: App Disabled Status Changed Handler")
        if(event.app != null && event.disabled != null)
            appsInstalledRepository.updateAppDisabledStatus(event.app!!,
                    event.disabled!!)
    }

    /**
     * Geofence Saved Event Handler
     */
    private fun geofenceSavedEventHandler(geofenceSavedDTO: GeofenceSavedDTO) {
        Timber.d("SSE: Geofence Saved Event")
        saveGeofenceInteract(SaveGeofenceInteract.Params(
                geofenceSavedDTO.identity, geofenceSavedDTO.name,
                geofenceSavedDTO.address, geofenceSavedDTO.lat,
                geofenceSavedDTO.log, geofenceSavedDTO.radius,
                geofenceSavedDTO.expirationDuration, geofenceSavedDTO.type,
                geofenceSavedDTO.kid, geofenceSavedDTO.createAt,
                geofenceSavedDTO.updateAt
        )){
            it.either(fun(_: Failure){
                Timber.d("Save Geofence Failed")
            }, fun(_: Unit) {
                Timber.d("Save Geofence Success")
            })
        }
    }

    /**
     * Geofence Deleted Event Handler
     */
    private fun geofenceDeletedEventHandler(geofenceDeletedDTO: GeofenceDeletedDTO) {
        Timber.d("SSE: Geofence Deleted Event")
        removeGeofenceInteract(RemoveGeofenceInteract.Params(
                arrayListOf(geofenceDeletedDTO.identity))){
            it.either(fun(_: Failure){
                Timber.d("Delete Geofence Failed")
            }, fun(_: Unit) {
                Timber.d("Delete Geofence Success")
            })
        }
    }

    /**
     * Fun Time Updated Event Handler
     */
    private fun funTimeUpdatedEventHandler(funTimeScheduledDTO: FunTimeScheduledDTO) {
        Timber.d("SSE: Fun Time Updated Event Handler")
        preferenceRepository.setFunTimeEnabled(funTimeScheduledDTO.enabled)

        if(!funTimeScheduledDTO.monday.day.isNullOrEmpty())
            checkCurrentFunTimeDayScheduledFor(funTimeScheduledDTO.monday.day!!,
                    funTimeScheduledDTO.monday.totalHours)

        if(!funTimeScheduledDTO.tuesday.day.isNullOrEmpty())
            checkCurrentFunTimeDayScheduledFor(funTimeScheduledDTO.tuesday.day!!,
                    funTimeScheduledDTO.tuesday.totalHours)

        if(!funTimeScheduledDTO.wednesday.day.isNullOrEmpty())
            checkCurrentFunTimeDayScheduledFor(funTimeScheduledDTO.wednesday.day!!,
                    funTimeScheduledDTO.wednesday.totalHours)

        if(!funTimeScheduledDTO.thursday.day.isNullOrEmpty())
            checkCurrentFunTimeDayScheduledFor(funTimeScheduledDTO.thursday.day!!,
                    funTimeScheduledDTO.thursday.totalHours)

        if(!funTimeScheduledDTO.friday.day.isNullOrEmpty())
            checkCurrentFunTimeDayScheduledFor(funTimeScheduledDTO.friday.day!!,
                    funTimeScheduledDTO.friday.totalHours)

        if(!funTimeScheduledDTO.saturday.day.isNullOrEmpty())
            checkCurrentFunTimeDayScheduledFor(funTimeScheduledDTO.saturday.day!!,
                    funTimeScheduledDTO.saturday.totalHours)

        if(!funTimeScheduledDTO.sunday.day.isNullOrEmpty())
            checkCurrentFunTimeDayScheduledFor(funTimeScheduledDTO.sunday.day!!,
                    funTimeScheduledDTO.sunday.totalHours)
        
        // Save Fun Time Day Scheduled
        funTimeDayScheduledRepository.save(Arrays.asList(
                // Add Monday
                FunTimeDayScheduledEntity(
                        day = funTimeScheduledDTO.monday.day,
                        enabled = funTimeScheduledDTO.monday.enabled,
                        totalHours = funTimeScheduledDTO.monday.totalHours
                ),
                // Add Thursday
                FunTimeDayScheduledEntity(
                        day = funTimeScheduledDTO.thursday.day,
                        enabled = funTimeScheduledDTO.thursday.enabled,
                        totalHours = funTimeScheduledDTO.thursday.totalHours
                ),
                // Add Wednesday
                FunTimeDayScheduledEntity(
                        day = funTimeScheduledDTO.wednesday.day,
                        enabled = funTimeScheduledDTO.wednesday.enabled,
                        totalHours = funTimeScheduledDTO.wednesday.totalHours
                ),
                // Add Tuesday
                FunTimeDayScheduledEntity(
                        day = funTimeScheduledDTO.tuesday.day,
                        enabled = funTimeScheduledDTO.tuesday.enabled,
                        totalHours = funTimeScheduledDTO.tuesday.totalHours
                ),
                // Add Friday
                FunTimeDayScheduledEntity(
                        day = funTimeScheduledDTO.friday.day,
                        enabled = funTimeScheduledDTO.friday.enabled,
                        totalHours = funTimeScheduledDTO.friday.totalHours
                ),
                // Add Saturday
                FunTimeDayScheduledEntity(
                        day = funTimeScheduledDTO.saturday.day,
                        enabled = funTimeScheduledDTO.saturday.enabled,
                        totalHours = funTimeScheduledDTO.saturday.totalHours
                ),
                // Add Sunday
                FunTimeDayScheduledEntity(
                        day = funTimeScheduledDTO.sunday.day,
                        enabled = funTimeScheduledDTO.sunday.enabled,
                        totalHours = funTimeScheduledDTO.sunday.totalHours
                )
        ))

    }


    /**
     * Check Current Fun Time Day Scheduled For
     */
    private fun checkCurrentFunTimeDayScheduledFor(day: String, totalHours: Long) {

        val currentFunTimeDayScheduled = preferenceRepository.getCurrentFunTimeDayScheduled()

        if (currentFunTimeDayScheduled == day) {

            // Get Fun Time Day Scheduled For Day
            funTimeDayScheduledRepository.getFunTimeDayScheduledForDay(day)?.let { dayScheduledSaved ->

                // Get Remaining Fun Time
                var remainingFunTime = preferenceRepository.getRemainingFunTime()


                if (dayScheduledSaved.totalHours != totalHours) {

                    // subtract seconds
                    if (dayScheduledSaved.totalHours > totalHours) {

                        if (remainingFunTime > 0) {
                            remainingFunTime = Math.max(0, remainingFunTime - ((dayScheduledSaved.totalHours -
                                    totalHours) * 60 * 60))
                        }

                    } else {

                        remainingFunTime += ((totalHours -
                                dayScheduledSaved.totalHours) * 60 * 60)

                    }

                    preferenceRepository.setRemainingFunTime(remainingFunTime)

                }

            }
        }
    }

    /**
     * Fun Time Day Scheduled Updated Event
     */
    private fun funTimeDayScheduledUpdatedEventHandler(funTimeDayScheduledChangedDTO: FunTimeDayScheduledChangedDTO) {
        Timber.d("SSE: Fun Time Updated Event Handler")

        if(!funTimeDayScheduledChangedDTO.day.isNullOrEmpty())
            checkCurrentFunTimeDayScheduledFor(funTimeDayScheduledChangedDTO.day!!, funTimeDayScheduledChangedDTO.totalHours)

        funTimeDayScheduledRepository.save(FunTimeDayScheduledEntity(
                day = funTimeDayScheduledChangedDTO.day,
                enabled = funTimeDayScheduledChangedDTO.enabled,
                totalHours = funTimeDayScheduledChangedDTO.totalHours
        ))
    }

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
                            .getPrefTerminalIdentity()))
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
        Timber.d("SSE: On Message -> id: %s", id)
        Timber.d("SSE: On Message -> event: %s", event)
        Timber.d("SSE: On Message -> message: %s", message)

        message?.let {eventMessage ->
            val jsonNode = objectMapper.readTree(eventMessage)
            if(jsonNode.has("event_type")) {
                val eventType = jsonNode.get("event_type").asText()
                try {

                    when(ServerEventTypeEnum.valueOf(eventType)) {
                        // Delete All Scheduled Block Event
                        ServerEventTypeEnum.DELETE_ALL_SCHEDULED_BLOCK_EVENT -> deleteAllScheduledBlockEventHandler(
                                objectMapper.readValue(eventMessage,
                                        DeleteAllScheduledBlockDTO::class.java))
                        // Delete Scheduled Block Event
                        ServerEventTypeEnum.DELETE_SCHEDULED_BLOCK_EVENT -> deleteScheduledBlockEventHandler(
                                objectMapper.readValue(eventMessage,
                                        DeleteScheduledBlockDTO::class.java)
                        )
                        // Scheduled Block Image Changed Event
                        ServerEventTypeEnum.SCHEDULED_BLOCK_IMAGE_CHANGED_EVENT -> scheduledBlockImageChangeEventHandler(
                                objectMapper.readValue(eventMessage,
                                        ScheduledBlockImageChangedDTO::class.java)
                        )
                        // Scheduled Block Saved Event
                        ServerEventTypeEnum.SCHEDULED_BLOCK_SAVED_EVENT -> scheduledBlockSavedEventHandler(
                                objectMapper.readValue(eventMessage,
                                        ScheduledBlockSavedDTO::class.java)
                        )
                        // Scheduled Block Status Change Event
                        ServerEventTypeEnum.SCHEDULED_BLOCK_STATUS_CHANGED_EVENT -> scheduledBlockStatusChangedEventHandler(
                                objectMapper.readValue(eventMessage,
                                        ScheduledBlockStatusChangedDTO::class.java)
                        )
                        // App Rules List Saved Event
                        ServerEventTypeEnum.APP_RULES_LIST_SAVED_EVENT -> appRulesListSavedEventHandler(
                                objectMapper.readValue(eventMessage,
                                        AppRulesListSavedDTO::class.java)
                        )
                        // App Rules Saved Event
                        ServerEventTypeEnum.APP_RULES_SAVED_EVENT -> appRulesSavedEventHandler(
                                objectMapper.readValue(eventMessage,
                                        AppRulesSavedDTO::class.java)
                        )
                        // Unlink Terminal Event
                        ServerEventTypeEnum.UNLINK_TERMINAL_EVENT ->
                            unlinkTerminal()
                        // Terminal Bed Time Status Changed
                        ServerEventTypeEnum.TERMINAL_BED_TIME_STATUS_CHANGED ->
                            bedTimeStatusChangedHandler(
                                    objectMapper.readValue(eventMessage,
                                            TerminalBedTimeStatusChangedDTO::class.java))
                        // Terminal Camera Status Changed
                        ServerEventTypeEnum.TERMINAL_CAMERA_STATUS_CHANGED ->
                            terminalCameraStatusChangedHandler(
                                    objectMapper.readValue(eventMessage,
                                            TerminalCameraStatusChangedDTO::class.java))
                        // Terminal Screen Status Changed
                        ServerEventTypeEnum.TERMINAL_SCREEN_STATUS_CHANGED ->
                            terminalScreenStatusChangedHandler(
                                    objectMapper.readValue(eventMessage,
                                            TerminalScreenStatusChangedDTO::class.java))
                        // App Disabled Status Changed
                        ServerEventTypeEnum.APP_DISABLED_STATUS_CHANGED ->
                            appDisabledStatusChangedHandler(
                                    objectMapper.readValue(eventMessage,
                                            AppDisabledStatusChangedDTO::class.java))
                        // Geofence Save Event
                        ServerEventTypeEnum.GEOFENCE_SAVED_EVENT -> {
                            geofenceSavedEventHandler(
                                    objectMapper.readValue(eventMessage,
                                            GeofenceSavedDTO::class.java))
                        }
                        // Geofence Deleted Event
                        ServerEventTypeEnum.GEOFENCE_DELETED_EVENT -> {
                            geofenceDeletedEventHandler(
                                    objectMapper.readValue(eventMessage,
                                            GeofenceDeletedDTO::class.java))
                        }
                        // Fun Time Updated Event
                        ServerEventTypeEnum.FUN_TIME_UPDATED_EVENT -> {
                            funTimeUpdatedEventHandler(objectMapper.readValue(eventMessage,
                                    FunTimeScheduledDTO::class.java))
                        }
                        // Fun Time Day Scheduled Updated Event
                        ServerEventTypeEnum.FUN_TIME_DAY_SCHEDULED_UPDATED_EVENT -> {
                            funTimeDayScheduledUpdatedEventHandler(
                                    objectMapper.readValue(eventMessage,
                                            FunTimeDayScheduledChangedDTO::class.java))
                        }
                        // Unknown Event
                        else -> Timber.d("SSE: Unknow Event")

                    }
                } catch(ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
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
            Timber.d("Monitoring Service Exception Handler -> %s", e.message)
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