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
import com.sanchez.sanchez.bullkeeper_kids.data.entity.AppRuleEnum
import com.sanchez.sanchez.bullkeeper_kids.data.entity.ScheduledBlockEntity
import com.sanchez.sanchez.bullkeeper_kids.data.net.utils.ApiEndPointsHelper
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IAppsInstalledRepository
import com.sanchez.sanchez.bullkeeper_kids.data.repository.impl.ScheduledBlocksRepositoryImpl
import com.sanchez.sanchez.bullkeeper_kids.data.sse.*
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SynchronizeTerminalCallHistoryInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts.SynchronizeTerminalContactsInteract
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
import com.sanchez.sanchez.bullkeeper_kids.presentation.lockscreen.LockScreenActivity
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import java.lang.Exception

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
                    LockScreenActivity.UNLOCK_APP_ACTION))
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
                Timber.d("CHECK_FOREGROUND Current App Foreground -> %s", currentAppForeground)
                if (!currentAppForeground.isNullOrEmpty()
                        && (!currentAppLocked.isNullOrEmpty() || currentAppForeground != packageName)){

                    val startBedTime = LocalTime(START_BED_TIME_HOUR, 0)
                    val endBedTime = LocalTime(END_BED_TIME_HOUR, 0)
                    val currentTime = LocalTime.now()

                    if(currentTime.isAfter(startBedTime)
                            && currentTime.isBefore(endBedTime)) {
                        sendUnLockAppAction()
                        if(preferenceRepository.isBedTimeEnabled()) {
                            isBedTimeEnabled = true
                            navigator.showBedTimeScreen(this)
                        } else {
                            // Disable Bed Time is needed
                            sendDisableBedTimeAction()
                        }
                    } else {

                        // Disable Bed Time is needed
                        sendDisableBedTimeAction()

                        // Get information about the application in the BD
                        Timber.d("CHECK_FOREGROUND -> Current App in foreground -> %s ", currentAppForeground)

                        val appInstalledEntity =
                                appsInstalledRepository.findByPackageName(currentAppForeground)

                        appInstalledEntity?.let {

                            if(it.appRule != null) {

                                try {

                                    val appRuleEnum = AppRuleEnum.valueOf(it.appRule!!)

                                    when(appRuleEnum) {

                                        /**
                                         * App Rule Per Scheduler
                                         */
                                        AppRuleEnum.PER_SCHEDULER -> {
                                            Timber.d("CHECK_FOREGROUND -> PER_SCHEDULER rule applied to the current application ")


                                            val anyScheduledBlockEnable =
                                                    scheduledBlocksRepositoryImpl.anyScheduledBlockEnableForThisMoment(
                                                            getString(R.string.joda_local_time_format_server_response))


                                            if(anyScheduledBlockEnable) {
                                                sendUnLockAppAction()
                                            } else {
                                                Timber.d("CHECK_FOREGROUND -> Lock Current Foreground app: %s", currentAppForeground)
                                                currentAppLocked = currentAppForeground
                                                navigator.showLockScreen(this, it.packageName,
                                                        it.appName, it.icon, it.appRule)
                                            }

                                        }

                                        /**
                                         * Always Allowed
                                         */
                                        AppRuleEnum.ALWAYS_ALLOWED -> {
                                            Timber.d("CHECK_FOREGROUND -> ALWAYS_ALLOWED rule applied to the current application ")
                                            sendUnLockAppAction()
                                        }

                                        /**
                                         * Never Allowed
                                         */
                                        AppRuleEnum.NEVER_ALLOWED -> {
                                            Timber.d("CHECK_FOREGROUND -> NEVER_ALLOWED rule applied to the current application ")
                                            currentAppLocked = currentAppForeground
                                            navigator.showLockScreen(this, it.packageName,
                                                    it.appName, it.icon, it.appRule)
                                        }

                                    }


                                } catch (ex: Exception) {
                                    Timber.d("CHECK_FOREGROUND -> ex: %s", ex.message)
                                    ex.printStackTrace()
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
                    location?.let { saveCurrentLocation(it) }
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
        scheduledBlocksRepositoryImpl.save(ScheduledBlockEntity(event.identity,
                event.name, event.enable, event.repeatable,
                event.image, event.kid, event.startAt?.toString(fmt),
                event.endAt?.toString(fmt), event.weeklyFrequency?.joinToString(",")))
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
        Timber.d("SSE: Scheduled Block Status Changed Event")
        event.appRulesList.forEach {
            if(it.identity != null && it.type != null)
                appsInstalledRepository.updateAppRule(it.identity!!, it.type!!)
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
     * Change bed time status
     */
    private fun changeBedTimeStatusHandler(event: ChangeBedTimeStatusDTO) {
        Timber.d("SSE: Change Bed Time Status")
        event.enabled?.let { preferenceRepository.setBedTimeEnabled(it) }
    }

    /**
     * Change Lock Screen Status
     */
    private fun changeLockScreenStatusHandler(event: ChangeLockScreenStatusDTO) {
        Timber.d("SSE: Change Lock Screen Status")
        event.enabled?.let { preferenceRepository.setLockScreenEnabled(it) }
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
            if(jsonNode.has("eventType")) {
                val eventType = jsonNode.get("eventType").asText()
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
                        // Change Bed Time Status Event
                        ServerEventTypeEnum.CHANGE_BED_TIME_STATUS_EVENT -> changeBedTimeStatusHandler(
                                objectMapper.readValue(eventMessage,
                                        ChangeBedTimeStatusDTO::class.java)
                        )
                        // Change Lock Screen Status Event
                        ServerEventTypeEnum.CHANGE_LOCK_SCREEN_STATUS_EVENT ->
                            changeLockScreenStatusHandler(
                                objectMapper.readValue(eventMessage,
                                        ChangeLockScreenStatusDTO::class.java)
                        )
                        // Unlink Terminal Event
                        ServerEventTypeEnum.UNLINK_TERMINAL_EVENT ->
                            unlinkTerminal()
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