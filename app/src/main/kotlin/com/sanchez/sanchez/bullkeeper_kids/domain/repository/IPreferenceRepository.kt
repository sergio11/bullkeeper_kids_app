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

}