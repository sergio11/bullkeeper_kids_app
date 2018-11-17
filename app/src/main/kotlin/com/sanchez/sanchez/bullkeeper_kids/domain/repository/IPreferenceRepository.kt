package com.sanchez.sanchez.bullkeeper_kids.domain.repository

/**
 * Preferences Repository
 */
interface IPreferenceRepository {

    companion object {

        /**
         * App Tutorial Constants
         */
        const val PREF_IS_TUTORIAL_COMPLETED = "IS_TUTORIAL_COMPLETED"
        const val IS_TUTORIAL_COMPLETED_DEFAULT_VALUE = false

    }

    /**
     * Is Tutorial Completed
     */
    fun isTutorialCompleted(): Boolean

    /**
     * Set Tutorial Completed
     */
    fun setTutorialCompleted(isTutorialCompleted: Boolean)


}