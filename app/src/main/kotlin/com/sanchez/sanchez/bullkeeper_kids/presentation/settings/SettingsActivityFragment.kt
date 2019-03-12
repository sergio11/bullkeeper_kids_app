package com.sanchez.sanchez.bullkeeper_kids.presentation.settings

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.SettingsComponent
import com.sanchez.sanchez.bullkeeper_kids.core.permission.IPermissionManager
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IGeolocationService
import com.sanchez.sanchez.bullkeeper_kids.services.IUsageStatsService
import kotlinx.android.synthetic.main.fragment_settings.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

/**
 * Settings Activity Fragment
 */
class SettingsActivityFragment : BaseFragment(), IPermissionManager.OnCheckPermissionListener {

    /**
     * Settings Activity Handler
     */
    private lateinit var activityHandler: ISettingsActivityHandler

    /**
     * Settings View Model
     */
    @Inject
    internal lateinit var settingsViewModel: SettingsViewModel

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Permission Manager
     */
    @Inject
    internal lateinit var permissionManager: IPermissionManager

    /**
     * Usage Stats Service
     */
    @Inject
    internal lateinit var usageStatsService: IUsageStatsService

    /**
     * Context
     */
    @Inject
    internal lateinit var context: Context

    /**
     * Geolocation Service
     */
    @Inject
    internal lateinit var geolocationService: IGeolocationService

    /**
     * State
     * =============
     */

    /**
     * Request Admin Capabilities In Progress
     */
    private var requestAdminCapabilitiesInProgress: Boolean = false

    /**
     * Request Usage Stats In Progress
     */
    private var requestUsageStatsInProgress: Boolean = false

    /**
     * Request Hight Accuraccy Location In Progress
     */
    private var requestHighAccuraccyLocationInProgress: Boolean = false

    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_settings


    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is ISettingsActivityHandler)
            throw IllegalArgumentException("Context should implement ISettingsActivityHandler")

        activityHandler = context

    }

    /**
     * On Start
     */
    override fun onStart() {
        super.onStart()

        if(requestAdminCapabilitiesInProgress && activityHandler.isDevicePolicyManagerActive()) {
            activityHandler.showNoticeDialog(R.string.sixth_page_admin_is_granted)
            adminAccessSwitch.isEnabled =
                    !activityHandler.isDevicePolicyManagerActive()
            preferenceRepository.setAdminAccessEnabled(activityHandler.isDevicePolicyManagerActive())
        }

        if(requestUsageStatsInProgress && usageStatsService.isUsageStatsAllowed()) {
            activityHandler.showNoticeDialog(R.string.fifth_page_usage_stats_granted)
            deviceStatisticsSwitch.isEnabled =
                    !usageStatsService.isUsageStatsAllowed()
            preferenceRepository.setUsageStatsAllowed(usageStatsService.isUsageStatsAllowed())
        }

        if(requestHighAccuraccyLocationInProgress && geolocationService.isHighAccuraccyLocationEnabled()) {
            activityHandler.showNoticeDialog(R.string.seventh_page_location_already_allowed)
            highPrecisionGeolocationSwitch.isEnabled =
                    !geolocationService.isHighAccuraccyLocationEnabled()
        }
    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()

        permissionManager.setCheckPermissionListener(this)

        // Location History Switch Handler

        locationHistorySwitch.isOn =
                !permissionManager.shouldAskPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        locationHistorySwitch.isEnabled =
                permissionManager.shouldAskPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        locationHistorySwitch.setOnToggledListener { toggleableView, isOn ->

            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionManager.checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        getString(R.string.third_page_location_history_reason))
            }

        }

        // Calls History

        callsHistorySwitch.isOn = !permissionManager.shouldAskPermission(Manifest.permission.READ_CALL_LOG)
        callsHistorySwitch.isEnabled = permissionManager.shouldAskPermission(Manifest.permission.READ_CALL_LOG)

        callsHistorySwitch.setOnToggledListener{toggleableView, isOn ->
            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.READ_CALL_LOG)) {
                permissionManager.checkSinglePermission(Manifest.permission.READ_CALL_LOG,
                        getString(R.string.third_page_call_logs_reason))
            }
        }

        // Contacts List

        contactsListSwitch.isOn = !permissionManager.shouldAskPermission(Manifest.permission.READ_CONTACTS)
        contactsListSwitch.isEnabled = permissionManager.shouldAskPermission(Manifest.permission.READ_CONTACTS)

        contactsListSwitch.setOnToggledListener{ toggleableView, isOn ->
            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.READ_CONTACTS)) {
                permissionManager.checkSinglePermission(Manifest.permission.READ_CONTACTS,
                        getString(R.string.third_page_contacts_reason))
            }
        }


        // Text Message Switch

        textMessageSwitch.isOn =
                !permissionManager.shouldAskPermission(Manifest.permission.READ_SMS)

        textMessageSwitch.isEnabled =
                permissionManager.shouldAskPermission(Manifest.permission.READ_SMS)

        textMessageSwitch.setOnToggledListener { toggleableView, isOn ->
            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.READ_SMS)) {
                permissionManager.checkSinglePermission(Manifest.permission.READ_SMS,
                        getString(R.string.third_page_text_messages_reason))
            }
        }

        // Storage Switch
        storageSwitch.isOn =
                !permissionManager.shouldAskPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)


        storageSwitch.isEnabled =
                permissionManager.shouldAskPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        storageSwitch.setOnToggledListener { toggleableView, isOn ->
            if(isOn && permissionManager.shouldAskPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionManager.checkSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        getString(R.string.third_page_storage_reason))
            }
        }

        // Device Statistics
        deviceStatisticsSwitch.isOn =
                usageStatsService.isUsageStatsAllowed()

        deviceStatisticsSwitch.isEnabled =
                !usageStatsService.isUsageStatsAllowed()

        deviceStatisticsSwitch.setOnToggledListener { toggleableView, isOn ->
            if(isOn && !usageStatsService.isUsageStatsAllowed()) {
                requestUsageStatsInProgress = true
                activityHandler.showUsageAccessSettings()
            } else {
                activityHandler.showNoticeDialog(R.string.fifth_page_usage_stats_already_allowed)
            }
        }

        // Admin Access Switch
        adminAccessSwitch.isOn =
                activityHandler.isDevicePolicyManagerActive()

        adminAccessSwitch.isEnabled =
                !activityHandler.isDevicePolicyManagerActive()

        adminAccessSwitch.setOnToggledListener { toggleableView, isOn ->
            if(isOn && !activityHandler.isDevicePolicyManagerActive()) {
                requestAdminCapabilitiesInProgress = true
                activityHandler.showDeviceAdminSettings()
            } else {
                activityHandler.showNoticeDialog(R.string.sixth_page_admin_is_already_enabled)
            }
        }

        // High Accuraccy Location Switch
        highPrecisionGeolocationSwitch.isOn =
                geolocationService.isHighAccuraccyLocationEnabled()

        highPrecisionGeolocationSwitch.isEnabled =
                !geolocationService.isHighAccuraccyLocationEnabled()

        highPrecisionGeolocationSwitch.setOnToggledListener { toggleableView, isOn ->
            if(isOn && !geolocationService.isHighAccuraccyLocationEnabled()) {
                requestAdminCapabilitiesInProgress = true
                activityHandler.showLocationSourceSettings()
            } else {
                activityHandler.showNoticeDialog(R.string.seventh_page_location_already_allowed)
            }
        }

    }

    /**
     * Change Switch Status
     */
    private fun changeSwitchStatus(permission: String, status: Boolean) {
        when(permission) {

            /**
             * Access Fine Location
             */
            Manifest.permission.ACCESS_FINE_LOCATION -> {
                locationHistorySwitch.isOn = status
                locationHistorySwitch.isEnabled = !status
                preferenceRepository.setAccessFineLocationEnabled(status)
            }

            /**
             * Read Contacts
             */
            Manifest.permission.READ_CONTACTS -> {
                contactsListSwitch.isOn = status
                contactsListSwitch.isEnabled = !status
                preferenceRepository.setReadContactsEnabled(status)
            }

            /**
             * Read Call Log
             */
            Manifest.permission.READ_CALL_LOG -> {
                callsHistorySwitch.isOn = status
                callsHistorySwitch.isEnabled = !status
                preferenceRepository.setReadCallLogEnabled(status)
            }

            /**
             * Read SMS
             */
            Manifest.permission.READ_SMS -> {
                textMessageSwitch.isOn = status
                textMessageSwitch.isEnabled = !status
                preferenceRepository.setReadSmsEnabled(status)
            }

            /**
             * Write External Storage
             */
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                storageSwitch.isOn = status
                storageSwitch.isEnabled = !status
                preferenceRepository.setWriteExternalStorageEnabled(status)
            }

        }
    }

    /**
     * On Single Permission Granted
     */
    override fun onSinglePermissionGranted(permission: String) {
        Preconditions.checkNotNull(permission, "Permission can not be null")
        Preconditions.checkState(permission.isNotEmpty(), "Permission can not be empty")
        changeSwitchStatus(permission, true)
    }

    /**
     * On Single Permission Rejected
     */
    override fun onSinglePermissionRejected(permission: String) {
        Preconditions.checkNotNull(permission, "Permission can not be null")
        Preconditions.checkState(permission.isNotEmpty(), "Permission can not be empty")
        changeSwitchStatus(permission, false)
    }

    /**
     * On Error Occurred
     */
    override fun onErrorOccurred(permission: String) {
        Preconditions.checkNotNull(permission, "Permission can not be null")
        Preconditions.checkState(permission.isNotEmpty(), "Permission can not be empty")
        changeSwitchStatus(permission, false)
    }

    /**
     * Initialize Injector
     */
    fun initializeInjector() {
        val settingsComponent = SettingsComponent::class.java
                .cast((activity as HasComponent<*>)
                        .component)

        settingsComponent?.inject(this)
    }

}
