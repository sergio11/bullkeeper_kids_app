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
        const val PREF_KID_IDENTITY = "kid"
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

        // Lock Screen Status
        const val PREF_LOCK_SCREEN = "lock_screen"
        const val LOCK_SCREEN_DEFAULT_VALUE = false

        // Lock Camera
        const val PREF_CAMERA_SCREEN = "lock_camera"
        const val LOCK_CAMERA_DEFAULT_VALUE = false

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
     * Is Lock Screen Enabled
     * @return
     */
    fun isLockScreenEnabled(): Boolean

    /**
     * Set Lock Screen Enabled
     * @param longitude
     */
    fun setLockScreenEnabled(isEnabled: Boolean)

    /**
     * Is Camera Enabled
     * @return
     */
    fun isCameraEnabled(): Boolean

    /**
     * Set Camera Enabled
     * @param longitude
     */
    fun setCameraEnabled(isEnabled: Boolean)


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


}