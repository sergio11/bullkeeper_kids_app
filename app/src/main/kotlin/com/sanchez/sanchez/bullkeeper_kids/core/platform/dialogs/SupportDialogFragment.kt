package com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs

import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.sanchez.sanchez.bullkeeper_kids.R

/**
 * Support Dialog Fragment
 */
abstract class SupportDialogFragment: DialogFragment() {

    val TITLE_ARG = "DIALOG_TITLE"

    /**
     * Title
     */
    lateinit var title: String

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        this.initializeInjector()
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            onPrepareArgs(arguments)
        }
    }

    /**
     * On Prepare Args
     * @param args
     */
    private fun onPrepareArgs(args: Bundle?) {
        title = args!!.getString(TITLE_ARG, getString(R.string.default_notice_dialog_title))
    }

    /**
     * On Create View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(getLayoutRes(), container)

    /**
     * On Resume
     */
    override fun onResume() {
        // Store access variables for window and blank point
        val window = dialog.window
        val size = Point()
        // Store dimensions of the screen in `size`
        val display = window!!.windowManager.defaultDisplay
        display.getSize(size)
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((size.x * 0.90).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        // Call super onResume after sizing
        super.onResume()
    }

    /**
     * Hide Keyboard
     */
    fun hideKeyboard() {
        if (context != null && view != null) {
            val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        }

    }

    /**
     * Get Layout Res
     * @return
     */
    protected abstract fun getLayoutRes(): Int


    /**
     * Initialize Injector
     */
    protected abstract fun initializeInjector()
}