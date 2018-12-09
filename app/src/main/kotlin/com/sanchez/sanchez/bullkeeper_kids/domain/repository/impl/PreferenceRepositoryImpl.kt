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
    @Inject constructor(private val context: Context): IPreferenceRepository {


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
                IPreferenceRepository.CURRENT_USER_IDENTITY_DEFAULT_VALUE)
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
                IPreferenceRepository.CURRENT_DEVICE_ID_DEFAULT_VALUE)
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
                IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE)
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
                IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE)
    }

    /**
     * Set Pref Kid Identity
     */
    override fun setPrefKidIdentity(kid: String) {
        mPref.edit().putString(IPreferenceRepository.PREF_KID_IDENTITY, kid).apply()
    }

}