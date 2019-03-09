package com.sanchez.sanchez.bullkeeper_kids.domain.repository

import com.sanchez.sanchez.bullkeeper_kids.domain.utils.IAuthTokenAware

/**
 * Preferences Repository
 */
interface IPreferenceRepository: IAuthTokenAware {

    companion object {

        /**
         * App Tutorial Constants
         */
        const val PREF_IS_TUTORIAL_COMPLETED = "IS_TUTORIAL_COMPLETED"
        const val IS_TUTORIAL_COMPLETED_DEFAULT_VALUE = false

        // Auth Token
        const val PREF_AUTH_TOKEN = "auth_token"
        const val AUTH_TOKEN_DEFAULT_VALUE = ""

        // Current User Identity
        const val PREF_CURRENT_USER_IDENTITY = "identity"
        const val CURRENT_USER_IDENTITY_DEFAULT_VALUE = ""

        // Terminal Identity
        const val PREF_TERMINAL_IDENTITY = "terminal_identity"
        const val TERMINAL_IDENTITY_DEFAULT_VALUE = ""

        // Device Id Identity
        const val PREF_DEVICE_ID = "device_id"
        const val CURRENT_DEVICE_ID_DEFAULT_VALUE = ""

        // Current User Identity
        const val PREF_KID_IDENTITY = "id"
        const val KID_IDENTITY_DEFAULT_VALUE = ""


        // Current Latitude
        const val PREF_CURRENT_LATITUDE = "current_latitude"
        const val CURRENT_LATITUDE_DEFAULT_VALUE = ""

        // Current Longitude
        const val PREF_CURRENT_LONGITUDE = "current_longitude"
        const val CURRENT_LONGITUDE_DEFAULT_VALUE = ""

        // Bed Time Status
        const val PREF_BED_TIME = "bed_time"
        const val BED_TIME_DEFAULT_VALUE = true

        // Screen Enabled
        const val PREF_SCREEN_ENABLED = "screen_enabled"
        const val SCREEN_ENABLED_DEFAULT_VALUE = true

        // Camera Enabled
        const val PREF_CAMERA_ENABLED = "camera_enabled"
        const val CAMERA_ENABLED_DEFAULT_VALUE = true

        // Settings Enabled
        const val PREF_SETTINGS_ENABLED = "settings_enabled"
        const val SETTINGS_ENABLED_DEFAULT_VALUE = false

        // SOS Request Expired At
        const val PREF_SOS_REQUEST_EXPIRED_AT = "sos_request_expired_at"
        const val SOS_REQUEST_EXPIRED_AT_DEFAULT_VALUE = ""

        // PickMe Up Expired At
        const val PREF_PICKME_UP_EXPIRED_AT = "pickme_up_expired_at"
        const val PICKME_UP_EXPIRED_AT_DEFAULT_VALUE = ""

        // Fun Time
        const val PREF_FUN_TIME = "fun_time"
        const val FUN_TIME_DEFAULT_VALUE = false

        // Current Fun Time Day Scheduled
        const val PREF_CURRENT_FUN_TIME_DAY_SCHEDULED = "fun_time_day_scheduled"
        const val CURRENT_FUN_TIME_DAY_SCHEDULED_VALUE = ""

        // Remaining fun time
        const val PREF_REMAINING_FUN_TIME = "remaining_fun_time"
        const val REMAINING_FUN_TIME_DEFAULT_VALUE = 0L

        // Time Bank
        const val PREF_TIME_BANK = "time_bank"
        const val TIME_BANK_DEFAUL_VALUE = 0L

        // Pref Access Fine Location
        const val PREF_ACCESS_FINE_LOCATION_ENABLED = "access_fine_location"
        const val ACCESS_FINE_LOCATION_ENABLED_DEFAULT_VALUE = false

        // Pref Read Contacts
        const val PREF_READ_CONTACTS_ENABLED = "read_contacts"
        const val READ_CONTACTS_ENABLED_DEFAULT_VALUE = false

        // Pref Read Call Log Enabled
        const val PREF_READ_CALL_LOG_ENABLED = "read_call_log"
        const val READ_CALL_LOG_ENABLED_DEFAULT_VALUE = false

        // Pref Read Sms Enabled
        const val PREF_READ_SMS_ENABLED = "read_sms"
        const val READ_SMS_ENABLED_DEFAULT_VALUE = false

        // Pref Write External Storage
        const val PREF_WRITE_EXTERNAL_STORAGE_ENABLED = "write_external"
        const val WRITE_EXTERNAL_STORAGE_DEFAULT_VALUE = false

        // Pref Usage Stats
        const val PREF_USAGE_STATS_ALLOWED = "usage_stats"
        const val USAGE_STATS_DEFAULT_VALUE = false

        // Pref Admin Access
        const val PREF_ADMIN_ACCESS_ALLOWED = "admin_access"
        const val ADMIN_ACCESS_DEFAULT_VALUE = false

        // Pref Battery Charging
        const val PREF_BATTERY_CHARGING_ENABLED = "battery_charging"
        const val BATTERY_CHARGING_DEFAULT_VALUE = false

        // Pref Battery Level
        const val PREF_BATTERY_LEVEL = "battery_level"
        const val BATTERY_LEVEL_DEFAULT_VALUE = 0
    }

    /**
     * Is Tutorial Completed
     */
    fun isTutorialCompleted(): Boolean

    /**
     * Set Tutorial Completed
     */
    fun setTutorialCompleted(isTutorialCompleted: Boolean)


    /**
     * Get Pref Current User Identity
     * @return
     */
    fun getPrefCurrentUserIdentity(): String

    /**
     * Set Pref Current Usre Identity
     * @param identity
     */
    fun setPrefCurrentUserIdentity(identity: String)

    /**
     * Get Pref Device Id
     * @return
     */
    fun getPrefDeviceId(): String

    /**
     * Set Pref Device Id
     * @param identity
     */
    fun setPrefDeviceId(deviceId: String)


    /**
     * Get Pref Terminal Identity
     * @return
     */
    fun getPrefTerminalIdentity(): String

    /**
     * Set Pref Terminal Identity
     * @param identity
     */
    fun setPrefTerminalIdentity(identity: String)


    /**
     * Get Pref Kid Identity
     * @return
     */
    fun getPrefKidIdentity(): String

    /**
     * Set Pref Kid Identity
     * @param identity
     */
    fun setPrefKidIdentity(kid: String)

    /**
     * Get Current Latitude
     * @return
     */
    fun getCurrentLatitude(): String

    /**
     * Set Current Latitude
     * @param latitude
     */
    fun setCurrentLatitude(latitude: String)


    /**
     * Get Current Longitude
     * @return
     */
    fun getCurrentLongitude(): String

    /**
     * Set Current Longitude
     * @param longitude
     */
    fun setCurrentLongitude(longitude: String)

    /**
     * Is Bed Time Enabled
     * @return
     */
    fun isBedTimeEnabled(): Boolean

    /**
     * Set Bed Time Enabled
     * @param longitude
     */
    fun setBedTimeEnabled(isEnabled: Boolean)

    /**
     * Is Screen Enabled
     * @return
     */
    fun isScreenEnabled(): Boolean

    /**
     * Set Screen Enabled
     * @param isEnabled
     */
    fun setScreenEnabled(isEnabled: Boolean)

    /**
     * Is Camera Enabled
     * @return
     */
    fun isCameraEnabled(): Boolean

    /**
     * Set Camera Enabled
     * @param isEnabled
     */
    fun setCameraEnabled(isEnabled: Boolean)

    /**
     * Is Settings Enabled
     * @return
     */
    fun isSettingsEnabled(): Boolean

    /**
     * Set Settings Enabled
     * @param isEnabled
     */
    fun setSettingsEnabled(isEnabled: Boolean)

    /**
     * Get SOS Request Expired At
     * @return
     */
    fun getSosRequestExpiredAt(): String

    /**
     * Set Sos Request Expired At
     * @param expiredAt
     */
    fun setSosRequestExpiredAt(expiredAt: String)

    /**
     * Get PickMeUp Request Expired At
     * @return
     */
    fun getPickMeUpRequestExpiredAt(): String

    /**
     * Set PickMe Up Request Expired At
     * @param expiredAt
     */
    fun setPickMeUpRequestExpiredAt(expiredAt: String)


    /**
     * Is Fun Time Enabled
     * @return
     */
    fun isFunTimeEnabled(): Boolean

    /**
     * Set Fun Time Enabled
     * @param is Enabled
     */
    fun setFunTimeEnabled(isEnabled: Boolean)

    /**
     * Get Current Fun Time Day Scheduled
     * @return
     */
    fun getCurrentFunTimeDayScheduled(): String

    /**
     * Set Current Fun Time Day Scheduled
     * @param dayScheduled
     */
    fun setCurrentFunTimeDayScheduled(dayScheduled: String)


    /**
     * Get Remaining Fun Time
     * @return
     */
    fun getRemainingFunTime(): Long

    /**
     * Set Remaining Fun Time
     * @param remainingFunTime
     */
    fun setRemainingFunTime(remainingFunTime: Long)

    /**
     * Get Time Bank
     * @return
     */
    fun getTimeBank(): Long

    /**
     * Set Time Bank
     * @param timeSaved
     */
    fun setTimeBank(timeSaved: Long)

    /**
     * Is Access Fine Location Enabled
     * @return
     */
    fun isAccessFineLocationEnabled(): Boolean

    /**
     * Set Access Fine Location Enabled
     * @param is Enabled
     */
    fun setAccessFineLocationEnabled(isEnabled: Boolean)

    /**
     * Is Read Contacts Enabled
     * @return
     */
    fun isReadContactsEnabled(): Boolean

    /**
     * Set Read Contacts
     * @param is Enabled
     */
    fun setReadContactsEnabled(isEnabled: Boolean)


    /**
     * Is Read Call Log
     * @return
     */
    fun isReadCallLogEnabled(): Boolean

    /**
     * Set Read Call Log
     * @param is Enabled
     */
    fun setReadCallLogEnabled(isEnabled: Boolean)

    /**
     * Is Read Sms
     * @return
     */
    fun isReadSmsEnabled(): Boolean

    /**
     * Set Read Sms Log
     * @param is Enabled
     */
    fun setReadSmsEnabled(isEnabled: Boolean)

    /**
     * Is Write External Storage
     * @return
     */
    fun isWriteExternalStorageEnabled(): Boolean

    /**
     * Set Write External Storage
     * @param is Enabled
     */
    fun setWriteExternalStorageEnabled(isEnabled: Boolean)

    /**
     * Is Usage Stats Allowed
     * @return
     */
    fun isUsageStatsAllowed(): Boolean

    /**
     * Set Usage Stats Allowed
     * @param is Enabled
     */
    fun setUsageStatsAllowed(isEnabled: Boolean)

    /**
     * Is Admin Access Enabled
     * @return
     */
    fun isAdminAccessEnabled(): Boolean

    /**
     * Set Admin Access Enabled
     * @param is Enabled
     */
    fun setAdminAccessEnabled(isEnabled: Boolean)

    /**
     * Set Battery Charging Enabled
     */
    fun setBatteryChargingEnabled(isEnabled: Boolean)

    /**
     * Is Battery Charging Enabled
     */
    fun isBatteryChargingEnabled(): Boolean

    /**
     * Set Battery Level
     */
    fun setBatteryLevel(level: Int)

    /**
     * Get Battery Level
     */
    fun getBatteryLevel(): Int


}