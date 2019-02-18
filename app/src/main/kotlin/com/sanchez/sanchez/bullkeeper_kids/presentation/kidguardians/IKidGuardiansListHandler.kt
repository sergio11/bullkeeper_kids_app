package com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians

import com.sanchez.sanchez.bullkeeper_kids.core.platform.IBasicActivityHandler
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidGuardianEntity

/**
 * Kid Guardians Handler
 */
interface IKidGuardiansListHandler: IBasicActivityHandler {

    /**
     * On Kid Guardian Selected
     */
    fun onKidGuardianSelected(kidGuardian: KidGuardianEntity)

}