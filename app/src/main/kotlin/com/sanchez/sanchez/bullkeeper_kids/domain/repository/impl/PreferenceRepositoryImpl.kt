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
}