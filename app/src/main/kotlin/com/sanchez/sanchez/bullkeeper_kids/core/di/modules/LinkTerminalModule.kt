package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import android.app.Application
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children.GetSelfChildrenInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.SaveTerminalInteract
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
    fun provideThirdLinkTerminalViewModel(application: Application,
                                          saveTerminalInteract: SaveTerminalInteract): ThirdLinkTerminalViewModel =
            ThirdLinkTerminalViewModel(application, saveTerminalInteract)

}