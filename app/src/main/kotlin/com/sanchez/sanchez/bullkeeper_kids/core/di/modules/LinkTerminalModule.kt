package com.sanchez.sanchez.bullkeeper_kids.core.di.modules

import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children.GetSelfChildrenInteract
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.SecondLinkTerminalViewModel
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
}