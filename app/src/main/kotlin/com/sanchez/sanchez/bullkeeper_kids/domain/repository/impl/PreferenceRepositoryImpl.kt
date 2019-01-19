package com.sanchez.sanchez.bullkeeper_kids.domain.repository.impl

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import javax.inject.Inject

/**
 * Preference Repository Impl
 */
class PreferenceRepositoryImpl
    @Inject constructor(context: Context): IPreferenceRepository {

    /**
     * Preferences
     */
    private val mPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)


    /**
     * Is Tutorial Completed
     */
    override fun isTutorialCompleted(): Boolean {
        return mPref.getBoolean(IPreferenceRepository.PREF_IS_TUTORIAL_COMPLETED,
                IPreferenceRepository.IS_TUTORIAL_COMPLETED_DEFAULT_VALUE)
    }

    /**
     * Set Tutorial Completed
     */
    override fun setTutorialCompleted(isTutorialCompleted: Boolean) {
        mPref.edit().putBoolean(IPreferenceRepository.PREF_IS_TUTORIAL_COMPLETED,
                isTutorialCompleted).apply()
    }

    /**
     * Get Auth Token
     */
    override fun getAuthToken(): String? {
        return mPref.getString(IPreferenceRepository.PREF_AUTH_TOKEN,
                IPreferenceRepository.AUTH_TOKEN_DEFAULT_VALUE)
    }

    /**
     * Set Auth Token
     */
    override fun setAuthToken(token: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_AUTH_TOKEN,
                token).apply()
    }

    /**
     * Get Pref Current User Identity
     */
    override fun getPrefCurrentUserIdentity(): String {
        return mPref.getString(IPreferenceRepository.PREF_CURRENT_USER_IDENTITY,
                IPreferenceRepository.CURRENT_USER_IDENTITY_DEFAULT_VALUE)!!
    }

    /**
     * Set Pref Current User Identity
     */
    override fun setPrefCurrentUserIdentity(identity: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_CURRENT_USER_IDENTITY,
                identity).apply()
    }

    /**
     * Get Pref Device Id
     */
    override fun getPrefDeviceId(): String {
        return mPref.getString(IPreferenceRepository.PREF_DEVICE_ID,
                IPreferenceRepository.CURRENT_DEVICE_ID_DEFAULT_VALUE)!!
    }

    /**
     * Set Pref Device Id
     */
    override fun setPrefDeviceId(deviceId: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_DEVICE_ID, deviceId).apply()
    }

    /**
     * Get Pref Terminal Identity
     */
    override fun getPrefTerminalIdentity(): String {
        return mPref.getString(IPreferenceRepository.PREF_TERMINAL_IDENTITY,
                IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE)!!
    }

    /**
     * Set Pref Terminal Identity
     */
    override fun setPrefTerminalIdentity(identity: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_TERMINAL_IDENTITY, identity).apply()
    }

    /**
     * Get Pref Kid Identity
     */
    override fun getPrefKidIdentity(): String {
        return mPref.getString(IPreferenceRepository.PREF_KID_IDENTITY,
                IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE)!!
    }

    /**
     * Set Pref Kid Identity
     */
    override fun setPrefKidIdentity(kid: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_KID_IDENTITY, kid).apply()
    }

    /**
     * Get Current Latitude
     */
    override fun getCurrentLatitude(): String {
        return mPref.getString(IPreferenceRepository.PREF_CURRENT_LATITUDE,
                IPreferenceRepository.CURRENT_LATITUDE_DEFAULT_VALUE)!!
    }

    /**
     * Set Current Latitude
     */
    override fun setCurrentLatitude(latitude: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_CURRENT_LATITUDE, latitude).apply()
    }

    /**
     * Get Current Longitude
     */
    override fun getCurrentLongitude(): String {
        return mPref.getString(IPreferenceRepository.PREF_CURRENT_LONGITUDE,
                IPreferenceRepository.CURRENT_LONGITUDE_DEFAULT_VALUE)!!
    }

    /**
     * Set Current Longitude
     */
    override fun setCurrentLongitude(longitude: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_CURRENT_LONGITUDE, longitude).apply()
    }

    /**
     * Is Bed Time Enabled
     */
    override fun isBedTimeEnabled(): Boolean {
        return mPref.getBoolean(IPreferenceRepository.PREF_BED_TIME,
                IPreferenceRepository.BED_TIME_DEFAULT_VALUE)
    }

    /**
     * Set Bed Time Enabled
     */
    override fun setBedTimeEnabled(isEnabled: Boolean) {
        mPref.edit().putBoolean(IPreferenceRepository.PREF_CURRENT_LONGITUDE,
                isEnabled).apply()
    }

    /**
     * Is Lock Screen Enabled
     */
    override fun isScreenEnabled(): Boolean {
        return mPref.getBoolean(IPreferenceRepository.PREF_SCREEN_ENABLED,
                IPreferenceRepository.SCREEN_ENABLED_DEFAULT_VALUE)
    }

    /**
     * Set Lock Screen Enabled
     */
    override fun setScreenEnabled(isEnabled: Boolean) {
        mPref.edit().putBoolean(IPreferenceRepository.PREF_SCREEN_ENABLED,
                isEnabled).apply()
    }

    /**
     * Is Camera Enabled
     */
    override fun isCameraEnabled(): Boolean {
        return mPref.getBoolean(IPreferenceRepository.PREF_CAMERA_ENABLED,
                IPreferenceRepository.CAMERA_ENABLED_DEFAULT_VALUE)
    }

    /**
     * Set Camera Enabled
     */
    override fun setCameraEnabled(isEnabled: Boolean) {
        mPref.edit().putBoolean(IPreferenceRepository.PREF_CAMERA_ENABLED,
                isEnabled).apply()
    }

    /**
     * Get SOS Request Expired At
     */
    override fun getSosRequestExpiredAt(): String {
        return mPref.getString(IPreferenceRepository.PREF_SOS_REQUEST_EXPIRED_AT,
                IPreferenceRepository.SOS_REQUEST_EXPIRED_AT_DEFAULT_VALUE)!!
    }

    /**
     * Set SOS Request Expired At
     */
    override fun setSosRequestExpiredAt(expiredAt: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_SOS_REQUEST_EXPIRED_AT, expiredAt).apply()
    }

    /**
     * Get Pick Me Up Request Expired At
     */
    override fun getPickMeUpRequestExpiredAt(): String {
        return mPref.getString(IPreferenceRepository.PREF_PICKME_UP_EXPIRED_AT,
                IPreferenceRepository.PICKME_UP_EXPIRED_AT_DEFAULT_VALUE)!!
    }

    /**
     * Set Pick Me Up Request Expired At
     */
    override fun setPickMeUpRequestExpiredAt(expiredAt: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_PICKME_UP_EXPIRED_AT, expiredAt).apply()
    }

    /**
     * Is Fun Time Enabled
     */
    override fun isFunTimeEnabled(): Boolean {
        return mPref.getBoolean(IPreferenceRepository.PREF_FUN_TIME,
                IPreferenceRepository.FUN_TIME_DEFAULT_VALUE)
    }

    /**
     * Set Fun Time Enabled
     */
    override fun setFunTimeEnabled(isEnabled: Boolean) {
        mPref.edit().putBoolean(IPreferenceRepository.PREF_FUN_TIME,
                isEnabled).apply()
    }

    /**
     * Get Remaining Fun Time
     */
    override fun getRemainingFunTime(): Long {
        return mPref.getLong(IPreferenceRepository.PREF_REMAINING_FUN_TIME,
                IPreferenceRepository.REMAINING_FUN_TIME_DEFAULT_VALUE)
    }

    /**
     * Set Remaining Fun Time
     */
    override fun setRemainingFunTime(remainingFunTime: Long) {
        mPref.edit().putLong(IPreferenceRepository.PREF_REMAINING_FUN_TIME,
                remainingFunTime).apply()
    }

    /**
     * Get Time Bank
     */
    override fun getTimeBank(): Long {
        return mPref.getLong(IPreferenceRepository.PREF_TIME_BANK,
                IPreferenceRepository.TIME_BANK_DEFAUL_VALUE)
    }

    /**
     * Set Time Bank
     */
    override fun setTimeBank(timeSaved: Long) {
        mPref.edit().putLong(IPreferenceRepository.PREF_TIME_BANK,
                timeSaved).apply()
    }

    /**
     * Get Current Fun Time Day Scheduled
     */
    override fun getCurrentFunTimeDayScheduled(): String {
        return mPref.getString(IPreferenceRepository.PREF_CURRENT_FUN_TIME_DAY_SCHEDULED,
                IPreferenceRepository.CURRENT_FUN_TIME_DAY_SCHEDULED_VALUE)!!
    }

    /**
     * Set Current Fun Time Day Scheduled
     */
    override fun setCurrentFunTimeDayScheduled(dayScheduled: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_CURRENT_FUN_TIME_DAY_SCHEDULED,
                dayScheduled).apply()
    }

    /**
     * Is Settings Enabled
     */
    override fun isSettingsEnabled(): Boolean {
        return mPref.getBoolean(IPreferenceRepository.PREF_SETTINGS_ENABLED,
                IPreferenceRepository.SETTINGS_ENABLED_DEFAULT_VALUE)
    }

    /**
     * Set Settings Enabled
     */
    override fun setSettingsEnabled(isEnabled: Boolean) {
        mPref.edit().putBoolean(IPreferenceRepository.PREF_SETTINGS_ENABLED,
                isEnabled).apply()
    }

}