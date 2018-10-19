package com.sanchez.sanchez.bullkeeper_kids.presentation.login

import android.content.Context
import android.content.Intent
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseActivity

/**
 * Login Activity
 */
class LoginActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) =
                Intent(context, LoginActivity::class.java)
    }

    /**
     * Get Fragment
     */
    override fun fragment() = LoginFragment()
}
