package com.sanchez.sanchez.bullkeeper_kids.presentation.timebank

import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.di.HasComponent
import com.sanchez.sanchez.bullkeeper_kids.core.di.components.ApplicationComponent
import com.sanchez.sanchez.bullkeeper_kids.core.extension.ToHoursMinutesSecondsFormat
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import com.sanchez.sanchez.bullkeeper_kids.core.sounds.ISoundManager
import com.sanchez.sanchez.bullkeeper_kids.data.repository.IFunTimeDayScheduledRepository
import com.sanchez.sanchez.bullkeeper_kids.domain.repository.IPreferenceRepository
import kotlinx.android.synthetic.main.fragment_time_bank.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Time Bank Activity Fragment
 */
class TimeBankActivityFragment : BaseFragment() {

    /**
     * Sound Manager
     */
    @Inject
    internal lateinit var soundManager: ISoundManager

    /**
     * Preference Repository
     */
    @Inject
    internal lateinit var preferenceRepository: IPreferenceRepository

    /**
     * Fun Time Day Scheduled Repository
     */
    @Inject
    internal lateinit var funTimeDayScheduledRepository: IFunTimeDayScheduledRepository


    /**
     * Layout Id
     */
    override fun layoutId(): Int = R.layout.fragment_time_bank

    /**
     * Time Bank Stream Id
     */
    private var timeBankStreamId: Int = -1

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeInjector()

        if(preferenceRepository.isFunTimeEnabled()) {

            val format = SimpleDateFormat("EEEE", Locale.US)
            val dayOfWeek = format.format(Calendar.getInstance().time)
                    .toUpperCase()

            funTimeDayScheduledRepository.getFunTimeDayScheduledForDay(dayOfWeek)?.let { dayScheduled ->

                if (dayScheduled.enabled) {

                    timeBankIcon.setImageResource(R.drawable.smile_white_solid)
                    timeBankDescription.text = getString(R.string.time_bank_avaliable_description)

                    val currentFunTimeDayScheduled = preferenceRepository
                            .getCurrentFunTimeDayScheduled()

                    if(dayScheduled.day !=  currentFunTimeDayScheduled) {
                        preferenceRepository.setCurrentFunTimeDayScheduled(dayScheduled.day!!)
                        preferenceRepository.setRemainingFunTime(dayScheduled.totalHours * 60 * 60)
                    }

                    val remainingFunTime = preferenceRepository.getRemainingFunTime()
                    val timeBankSaved = preferenceRepository.getTimeBank()

                    when {
                        remainingFunTime > 0 -> {

                            saveOnTimeBank.isEnabled = true
                            saveOnTimeBank.text = String.format(Locale.getDefault(),
                                    getString(R.string.time_bank_saved_button_text),
                                    remainingFunTime.ToHoursMinutesSecondsFormat())


                            saveOnTimeBank.setOnClickListener {
                                saveOnTimeBank.isEnabled = false
                                saveOnTimeBank.text = getString(R.string.time_bank_button_saved_successfully_text)
                                timeBankStreamId = soundManager.playSound(ISoundManager.TIME_BANK_SOUND)
                                preferenceRepository.setRemainingFunTime(IPreferenceRepository.TIME_BANK_DEFAUL_VALUE)
                                preferenceRepository.setTimeBank(timeBankSaved + remainingFunTime)
                            }


                        }
                        timeBankSaved > 0 -> {

                            saveOnTimeBank.isEnabled = true
                            saveOnTimeBank.text = String.format(Locale.getDefault(),
                                    getString(R.string.time_bank_restored_button_text),
                                    timeBankSaved.ToHoursMinutesSecondsFormat())

                            saveOnTimeBank.setOnClickListener {
                                saveOnTimeBank.isEnabled = false
                                saveOnTimeBank.text = getString(R.string.time_bank_button_restored_successfully_text)
                                timeBankStreamId = soundManager.playSound(ISoundManager.TIME_BANK_SOUND)
                                preferenceRepository.setRemainingFunTime(remainingFunTime + timeBankSaved)
                                preferenceRepository.setTimeBank(IPreferenceRepository.TIME_BANK_DEFAUL_VALUE)
                            }

                        }
                        else -> {

                            saveOnTimeBank.isEnabled = false
                            saveOnTimeBank.text = getString(R.string.time_bank_not_avaliable_btn)

                        }
                    }

                }

            }

        }

    }

    /**
     * On Destroy View
     */
    override fun onDestroyView() {
        super.onDestroyView()
        soundManager.stopSound(timeBankStreamId)
    }

    /**
     * Initialize Injector
     */
    fun initializeInjector() {
        val applicationComponent = ApplicationComponent::class.java
                .cast((activity as HasComponent<*>)
                        .component)
        applicationComponent?.inject(this)
    }
}
