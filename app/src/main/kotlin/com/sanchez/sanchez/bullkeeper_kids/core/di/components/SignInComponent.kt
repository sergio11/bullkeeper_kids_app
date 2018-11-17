package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.AuthModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.login.SignInFragment
import dagger.Component

/**
 * App Tutorial Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class, AuthModule::class])
interface SignInComponent: ActivityComponent {

    /**
     * Inject into Sign In Activity
     */
    fun inject(signInActivity: SignInActivity)

    /**
     * Inject into Sign In Fragment
     */
    fun inject(signInFragment: SignInFragment)
}