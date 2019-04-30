package com.sanchez.sanchez.bullkeeper_kids.presentation.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import at.grabner.circleprogress.CircleProgressView
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToHoursMinutesSecondsFormat
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IFunTimeDayScheduledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import com.sanchez.sanchez.bullkeeper_kids.presentation.services.MonitoringService.Companion.FUN_TIME_CHANGED_ACTION
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * A placeholder fragment containing a simple view.
 */
class HomeActivityFragment : BaseFragment() {

    /**
     * Activity Handler
     */
    private lateinit var activityHandler: IHomeActivityHandler

    /**
     * Preferences Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Fun Time Day Scheduled Repository
     */
    @Inject
    internal lateinit var funTimeDayScheduledRepository: IFunTimeDayScheduledRepository

    /**
     * Context
     */
    @Inject
    internal lateinit var context: Context

    /**
     * Sound Manager
     */
    @Inject
    internal lateinit var soundManager: ISoundManager

    /**
     * Views
     */


    private lateinit var pickMeUpAction: View
    private lateinit var timeBankAction: View
    private lateinit var sosAction: View
    private lateinit var chatAction: View
    private lateinit var funTimeTitle: TextView
    private lateinit var remainingFunTimeTextView: TextView
    private lateinit var funTimeIcon: ImageView
    private lateinit var funTimeDescription: TextView
    private lateinit var funTimeProgress: CircleProgressView


    /**
     * Fun Time Changed Event Handler
     */
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    private var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == FUN_TIME_CHANGED_ACTION) {
                try {
                    showFunTimeStatus()
                    soundManager.playSound(R.raw.send_message_sound)
                } catch (ex: Exception) {
                    Timber.d("Exception Ocurred")
                    ex.printStackTrace()
                }

            }
        }
    }

    /**
     * On Attach
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context !is IHomeActivityHandler)
            throw IllegalArgumentException("Context must implement IHome Activity Handler")

        activityHandler = context

    }

    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_home


    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()

        pickMeUpAction = view.findViewById(R.id.pickMeUpAction)
        timeBankAction = view.findViewById(R.id.timeBankAction)
        sosAction = view.findViewById(R.id.sosAction)
        chatAction = view.findViewById(R.id.chatAction)
        funTimeTitle = view.findViewById(R.id.funTimeTitle)
        remainingFunTimeTextView = view.findViewById(R.id.remainingFunTimeTextView)
        funTimeIcon = view.findViewById(R.id.funTimeIcon)
        funTimeDescription = view.findViewById(R.id.funTimeDescription)
        funTimeProgress = view.findViewById(R.id.funTimeProgress)

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context)
        val mIntentFilter = IntentFilter()
        mIntentFilter.addAction(FUN_TIME_CHANGED_ACTION)
        mLocalBroadcastManager?.registerReceiver(mBroadcastReceiver, mIntentFilter)


        // Pick Me Up click Handler
        pickMeUpAction.setOnClickListener {
            activityHandler.showPickMeUpScreen()
        }

        // Time Bank Action
        timeBankAction.setOnClickListener {
            activityHandler.showTimeBankScreen()
        }

        // SOS Action
        sosAction.setOnClickListener {
            activityHandler.showSosScreen()
        }

        // Chat Action
        chatAction.setOnClickListener {
            activityHandler.showChatAction()
        }


    }

    /**
     * On Resume
     */
    override fun onResume() {
        super.onResume()
        showFunTimeStatus()
    }

    /**
     *
     */
    override fun onDestroyView() {
        super.onDestroyView()
        mLocalBroadcastManager?.unregisterReceiver(mBroadcastReceiver)
    }

    /**
     * Show Fun Time Status
     */
    private fun showFunTimeStatus(){

        if(preferenceRepository.isFunTimeEnabled()) {

            val format = SimpleDateFormat("EEEE", Locale.US)
            val dayOfWeek = format.format(Calendar.getInstance().time)
                    .toUpperCase()

            funTimeDayScheduledRepository.getFunTimeDayScheduledForDay(dayOfWeek)?.let {dayScheduled ->

                if(dayScheduled.enabled) {

                    val currentFunTimeDayScheduled = preferenceRepository
                            .getCurrentFunTimeDayScheduled()

                    if(dayScheduled.day !=  currentFunTimeDayScheduled) {
                        preferenceRepository.setCurrentFunTimeDayScheduled(dayScheduled.day!!)
                        preferenceRepository.setRemainingFunTime(dayScheduled.totalHours * 60 * 60)
                    }

                    val remainingFunTime = preferenceRepository.getRemainingFunTime()

                    if(remainingFunTime > 0) {
                        // Set Fun Time Title
                        funTimeTitle.text = getString(R.string.fun_time_avaliable_title)
                        remainingFunTimeTextView.visibility = View.VISIBLE
                        remainingFunTimeTextView.text = remainingFunTime.ToHoursMinutesSecondsFormat()
                        funTimeIcon.setImageResource(R.drawable.smile_white_solid)
                        funTimeDescription.text = getString(R.string.fun_time_avaliable_description)

                        val remainingFunTimePercentage = 1.0f *  remainingFunTime / (dayScheduled.totalHours*60*60) * 100

                        funTimeProgress.setValue(remainingFunTimePercentage)

                        if(remainingFunTimePercentage >= 50) {
                            funTimeProgress.setBarColor(ContextCompat.getColor(context, R.color.greenSuccess))
                        } else if( remainingFunTimePercentage < 50 && remainingFunTimePercentage >= 20) {
                            funTimeProgress.setBarColor(ContextCompat.getColor(context, R.color.yellowWarning))
                        } else {
                            funTimeProgress.setBarColor(ContextCompat.getColor(context, R.color.redDanger))
                        }
                    } else {
                        showFunTimeNotAvaliable()
                    }

                } else {
                    showFunTimeNotAvaliable()
                }

            }

        } else {
            showFunTimeNotAvaliable()
        }
    }

    /**
     * Show Fun Time Not Avaliable
     */
    private fun showFunTimeNotAvaliable(){
        funTimeTitle.text = getString(R.string.fun_time_not_avaliable_title)
        remainingFunTimeTextView.visibility = View.GONE
        funTimeIcon.setImageResource(R.drawable.sad_face_icon)
        funTimeDescription.text = getString(R.string.fun_time_not_avaliable_description)
        funTimeProgress.setValue(0.0f)
    }

    /**
     * Initialize Injector
     */
    fun initializeInjector() {
        val appComponent = ApplicationComponent::class.java
                .cast((activity as HasComponent<*>)
                        .component)
        appComponent?.inject(this)
    }


}
