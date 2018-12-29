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

}