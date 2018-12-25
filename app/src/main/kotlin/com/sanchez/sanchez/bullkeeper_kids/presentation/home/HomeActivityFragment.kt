package com.sanchez.sanchez.bullkeeper_kids.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.platform.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.IllegalArgumentException

/**
 * A placeholder fragment containing a simple view.
 */
class HomeActivityFragment : BaseFragment() {


    private lateinit var activityHandler: IHomeActivityHandler

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

        // Profile Action
        profileAction.setOnClickListener {
            activityHandler.showBedTime()
        }
    }
}
