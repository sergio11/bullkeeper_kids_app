package com.sanchez.sanchez.bullkeeper_kids.core.di.components

import com.sanchez.sanchez.bullkeeper_kids.core.di.modules.*
import com.sanchez.sanchez.bullkeeper_kids.core.di.scopes.PerActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.LinkDeviceTutorialActivity
import com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages.*
import dagger.Component

/**
 * Link Device Tutorial Component
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [
            ActivityModule::class, TerminalModule::class,
            GuardianModule::class, LinkTerminalModule::class])
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

    /**
     * Inject into Third
     */
    fun inject(thirdLinkTerminalPageFragment: ThirdLinkTerminalPageFragment)

    /**
     * Inject into Four Link Terminal Page Fragment
     */
    fun inject(fourLinkTerminalPageFragment: FourLinkTerminalPageFragment)

    /**
     * Inject into Fifth link terminal page fragment
     */
    fun inject(fifthLinkTerminalPageFragment: FifthLinkTerminalPageFragment)

}