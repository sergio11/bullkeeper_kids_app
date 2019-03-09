package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ActionBootCompletedReceiverComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ShutdownReceiverComponent
import com.sanchez.sanchez.bullkeeper_kids.core.exception.Failure
import com.sanchez.sanchez.bullkeeper_kids.domain.interactors.terminal.SaveTerminalStatusInteract
import com.sanchez.sanchez.bullkeeper_kids.domain.models.TerminalStatusEnum
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Shutdown Broadcast Receiver
 */
class ShutdownBroadcastReceiver : BroadcastReceiver()  {

    /**
     * Shutdown Component
     */
    private val shutdownReceiverComponent: ShutdownReceiverComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.shutdownReceiverComponent
    }


    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Save Terminal Status Interact
     */
    @Inject
    internal lateinit var saveTerminalStatusInteract: SaveTerminalStatusInteract


    /**
     * On Receive
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        shutdownReceiverComponent.inject(this)

        Timber.d("BKA_62: Shutdown Broadcast Receiver called")

        if(preferenceRepository.getPrefKidIdentity()
                != IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE &&
                preferenceRepository.getPrefTerminalIdentity()
                    != IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE) {

            val kid = preferenceRepository.getPrefKidIdentity()
            val terminal = preferenceRepository.getPrefTerminalIdentity()

            saveTerminalStatusInteract(SaveTerminalStatusInteract.Params(
                    kid = kid,
                    terminal = terminal,
                    status = TerminalStatusEnum.STATE_OFF
            )){
                it.either(fnL = fun(_: Failure){
                    Timber.d("Save Terminal Status Failed")
                }, fnR = fun(_: Unit) {
                    Timber.d("Save Terminal Status Success")
                })
            }
        }
    }
}