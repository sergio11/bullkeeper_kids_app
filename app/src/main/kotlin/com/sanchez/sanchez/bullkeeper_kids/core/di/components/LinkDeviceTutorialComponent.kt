package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ActivityModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.LinkTerminalModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.ParentModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.TerminalModule
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.LinkDeviceTutorialActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.FirstLinkTerminalPageFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.SecondLinkTerminalPageFragment
import dagger.Component

/**
 * Link Device Tutorial Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [ActivityModule::class, TerminalModule::class,
            ParentModule::class, LinkTerminalModule::class])
interface LinkDeviceTutorialComponent: ActivityComponent {

    /**
     * Inject into Link Tutorial Activity
     */
    fun inject(linkDeviceTutorialActivity: LinkDeviceTutorialActivity)

    /**
     * Inject into First Link Terminal Page Fragment
     */
    fun inject(firstLinkTerminalPageFragment: FirstLinkTerminalPageFragment)

    /**
     * Inject into Second Link Terminal Page Fragment
     */
    fun inject(secondLinkTerminalPageFragment: SecondLinkTerminalPageFragment)

}