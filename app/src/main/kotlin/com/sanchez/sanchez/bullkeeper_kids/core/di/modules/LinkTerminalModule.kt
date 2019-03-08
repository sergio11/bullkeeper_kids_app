package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.app.Application
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.calls.SynchronizeTerminalCallHistoryInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children.GetSelfChildrenInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.contacts.SynchronizeTerminalContactsInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.packages.SynchronizeInstalledPackagesInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.sms.SynchronizeTerminalSMSInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.GetTerminalDetailInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.SaveTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.UnlinkTerminalInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.FirstLinkTerminalViewModel
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.FourLinkTerminalViewModel
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.SecondLinkTerminalViewModel
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.ThirdLinkTerminalViewModel
import dagger.Module
import dagger.Provides

/**
 * Link Terminal Module
 */
@Module
class LinkTerminalModule {

    /**
     * Provide Second Link Terminal View Model
     */
    @Provides
    @PerActivity
    fun provideSecondLinkTerminalViewModel(getSelfChildrenInteract: GetSelfChildrenInteract): SecondLinkTerminalViewModel =
            SecondLinkTerminalViewModel(getSelfChildrenInteract)

    /**
     * Provide Third Link Terminal View Model
     */
    @Provides
    @PerActivity
    fun provideThirdLinkTerminalViewModel(
            application: Application,
            saveTerminalInteract: SaveTerminalInteract,
            getTerminalDetailInteract: GetTerminalDetailInteract,
            preferenceRepository: IPreferenceRepository): ThirdLinkTerminalViewModel =
            ThirdLinkTerminalViewModel(application, saveTerminalInteract,
                    getTerminalDetailInteract, preferenceRepository)


    /**
     * Provide FirstLinkTerminalViewModel
     */
    @Provides
    @PerActivity
    fun provideFirstLinkTerminalViewModel(
            application: Application,
            getTerminalDetailInteract: GetTerminalDetailInteract,
            preferenceRepository: IPreferenceRepository,
            unlinkTerminalInteract: UnlinkTerminalInteract): FirstLinkTerminalViewModel
            = FirstLinkTerminalViewModel(application, getTerminalDetailInteract, preferenceRepository, unlinkTerminalInteract)


    /**
     * Provide Four Link Terminal ViewModel
     */
    @Provides
    @PerActivity
    fun provideFourLinkTerminalViewModel(
            application: Application,
            preferenceRepository: IPreferenceRepository,
            synchronizeInstalledPackagesInteract: SynchronizeInstalledPackagesInteract,
            synchronizeTerminalSMSInteract: SynchronizeTerminalSMSInteract,
            synchronizeTerminalCallHistoryInteract: SynchronizeTerminalCallHistoryInteract,
            synchronizeTerminalContactsInteract: SynchronizeTerminalContactsInteract): FourLinkTerminalViewModel
            = FourLinkTerminalViewModel(application,
                preferenceRepository, synchronizeInstalledPackagesInteract, synchronizeTerminalSMSInteract,
                synchronizeTerminalCallHistoryInteract, synchronizeTerminalContactsInteract)

}