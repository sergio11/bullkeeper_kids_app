package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.INavigator
import com.sanchez.sanchez.bullkeeper_kids.core.navigation.impl.NavigatorImpl
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.services.IAuthenticatorService
import com.sanchez.sanchez.bullkeeper_kids.services.ISystemPackageHelper
import com.sanchez.sanchez.bullkeeper_kids.services.impl.SystemPackageHelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Application Module
 */
@Module
class ApplicationModule(private val application: AndroidApplication) {

    /**
     * Provide Application Context
     */
    @Provides @Singleton fun provideApplicationContext(): Context = application

    /**
     * Provide System Package Helper
     */
    @Provides @Singleton fun provideSystemPackageHelper(context: Context): ISystemPackageHelper {
        return SystemPackageHelperImpl(context)
    }

    /**
     * Provide Navigator
     */
    @Provides @Singleton fun provideNavigator(authenticatorService: IAuthenticatorService,
                                              preferenceRepository: IPreferenceRepository):
            INavigator = NavigatorImpl(authenticatorService, preferenceRepository)

}
