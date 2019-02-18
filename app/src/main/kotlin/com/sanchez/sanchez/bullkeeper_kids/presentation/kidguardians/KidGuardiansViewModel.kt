package com.sanchez.sanchez.bullkeeper_kids.presentation.kidguardians

import android.arch.lifecycle.MutableLiveData
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.core.interactor.UseCase
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseViewModel
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.children.GetConfirmedGuardiansForKidInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.KidGuardianEntity
import com.sanchez.sanchez.bullkeeper_kids.domain.models.LoadingStateEnum
import javax.inject.Inject

/**
 * Kid Guardians View Model
 */
class KidGuardiansViewModel
    @Inject constructor(
            private val getConfirmedGuardiansForKidInteract: GetConfirmedGuardiansForKidInteract
    )
    : BaseViewModel()  {

    /**
     * Kid Guardians
     */
    val kidGuardians: MutableLiveData<List<KidGuardianEntity>> by lazy {
        MutableLiveData<List<KidGuardianEntity>>()
    }


    /**
     * State
     */
    val state: MutableLiveData<LoadingStateEnum> by lazy {
        MutableLiveData<LoadingStateEnum>()
    }

    /**
     * Init
     */
    override fun init() {
        state.value = LoadingStateEnum.LOADING
        kidGuardians.value = ArrayList()
    }

    /**
     * Get Confirmed Guardians For Kid Interact
     */
    fun load(){
        state.value = LoadingStateEnum.LOADING
        getConfirmedGuardiansForKidInteract(UseCase.None()){
            it.either(fnR = fun(kidGuardians: List<KidGuardianEntity>) {
                if(!kidGuardians.isNullOrEmpty()) {
                    this.kidGuardians.value = kidGuardians
                    state.value = LoadingStateEnum.DATA_FOUND
                } else {
                    state.value = LoadingStateEnum.NO_DATA_FOUND
                }
            }, fnL = fun(failure: Failure){
                when(failure) {
                    is GetConfirmedGuardiansForKidInteract.NoKidGuardianFoundFailure -> state.postValue(LoadingStateEnum.NO_DATA_FOUND)
                    else ->  state.postValue(LoadingStateEnum.ERROR)
                }
            })
        }
    }

}