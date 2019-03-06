package com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import com.sanchez.sanchez.bullkeeper_kids.AndroidApplication
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ActionBootCompletedReceiverComponent
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService
import timber.log.Timber
import javax.inject.Inject

/**
 * Action Boot Completed Broadcast Receiver
 */
class ActionBootCompletedBroadcastReceiver : BroadcastReceiver()  {

    /**
     * Action Boot Completed Component
     */
    private val actionBootCompletedComponent: ActionBootCompletedReceiverComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AndroidApplication.INSTANCE.actionBootCompletedReceiverComponent
    }


    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository


    /**
     * On Receive
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        actionBootCompletedComponent.inject(this)

        Timber.d("Action Boot Completed Event Fired")

        context?.let {

            if(preferenceRepository.getPrefKidIdentity()
                    != IPreferenceRepository.KID_IDENTITY_DEFAULT_VALUE &&
                    preferenceRepository.getPrefTerminalIdentity() !=
                    IPreferenceRepository.TERMINAL_IDENTITY_DEFAULT_VALUE)
                ContextCompat.startForegroundService(it,
                        Intent(it, MonitoringService::class.java))

        }


    }
}