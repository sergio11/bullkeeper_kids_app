package com.sanchez.sanchez.bullkeeper_kids.presentation.linkterminal.pages

import android.arch.lifecycle.MutableLiveData
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children.GetSelfChildrenInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.ChildrenOfSelfGuardianEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.SupervisedChildrenEntity
import javax.inject.Inject

/**
 * Sign In View Model
 */
class SecondLinkTerminalViewModel
    @Inject constructor(private val getSelfChildrenInteract: GetSelfChildrenInteract) : BaseViewModel()  {


    /**
     * Live Data
     */

    var childrenList: MutableLiveData<List<SupervisedChildrenEntity>> = MutableLiveData()

    /**
     * Load Children Failure
     */
    var loadChildrenFailure: MutableLiveData<Failure> = MutableLiveData()


    /**
     * Methods
     */

    fun loadChildrenList() =
        getSelfChildrenInteract(UseCase.None()){
            it.either(::onChildrenLoadedFailed, ::onChildrenLoaded)
        }

    /**
     * Handlers
     */

    /**
     * On Children Loaded Failed
     */
    private fun onChildrenLoadedFailed(failure: Failure) {
        Preconditions.checkNotNull(failure, "Failure can not be null")
        loadChildrenFailure.value = failure
    }

    /**
     * On Children Loaded
     */
    private fun onChildrenLoaded(children: ChildrenOfSelfGuardianEntity) {
        Preconditions.checkNotNull(children, "Children list can not be null")
        childrenList.value = children.supervisedChildrenList
    }

}