package com.sanchez.sanchez.bullkeeper_kids.presentation.home

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.core.extension.showShortMessage
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A placeholder fragment containing a simple view.
 */
class HomeActivityFragment : Fragment() {

    /**
     * On Create View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pick Me Up click Handler
        pickMeUpAction.setOnClickListener {
            context!!.showShortMessage("Pick Me Up Clicked")
        }

        // Time Bank Action
        timeBankAction.setOnClickListener {
            context!!.showShortMessage("Time Banck Clicked")
        }

        // SOS Action
        sosAction.setOnClickListener {
            context!!.showShortMessage("Sos Action clicked")
        }

        // Profile Action
        profileAction.setOnClickListener {
            context!!.showShortMessage("Profile Action clicked")
        }


    }
}
