package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.core.permission.IPermissionManager
import com.sanchez.sanchez.bullkeeper_kids.core.permission.PermissionManagerImpl
import dagger.Module
import dagger.Provides
/**
 * A module to wrap all the dependencies of an activity
 */
@Module
class ActivityModule
    constructor(private val activity: AppCompatActivity) {

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    fun provideActivityCompat(): AppCompatActivity {
        return this.activity
    }

    /**
     * Provide Activity
     */
    @Provides
    @PerActivity
    fun provideActivity(): Activity {
        return this.activity
    }

    /**
     * Expose the IPermissionManager to dependents in the graph.
     */
    @Provides
    @PerActivity
    fun providePermissionManager(): IPermissionManager {
        return PermissionManagerImpl(activity)
    }

}