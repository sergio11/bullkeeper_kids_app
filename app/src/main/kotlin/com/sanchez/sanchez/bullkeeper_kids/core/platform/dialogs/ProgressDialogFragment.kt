package com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import kotlinx.android.synthetic.main.progress_dialog_layout.*
import java.lang.ref.WeakReference

/**
 * Progress Dialog Fragment
 */
class ProgressDialogFragment: SupportDialogFragment() {

    /**
     *
     */
    companion object {

        /**
         * Tag
         */
        val TAG = "PROGRESS_DIALOG_FRAGMENT"

        /**
         * Title Arg
         */
        val TITLE_ARG = "DIALOG_TITLE"

        /**
         * Current Progress Dialog
         */
        private var currentProgressDialog: WeakReference<ProgressDialogFragment>? = null


        /**
         * Show Dialog
         * @param activity
         * @return
         */
        @JvmStatic
        fun showDialog(activity: AppCompatActivity, title: String): ProgressDialogFragment {
            val progressDialog = ProgressDialogFragment()
            progressDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CommonDialogFragmentTheme)
            // Config Arguments
            val args = Bundle()
            args.putString(TITLE_ARG, title)
            progressDialog.arguments = args
            progressDialog.show(activity.supportFragmentManager, TAG)
            currentProgressDialog = WeakReference(progressDialog)
            progressDialog.isCancelable = false
            return progressDialog
        }

        /**
         * Cancel Current Progress Dialog
         */
        @JvmStatic
        fun cancelCurrent() {
            currentProgressDialog?.get()?.dismiss()
            currentProgressDialog?.clear()
            currentProgressDialog = null
        }

    }

    /**
     * Get Layout Res
     * @return
     */
    override fun getLayoutRes(): Int = R.layout.progress_dialog_layout

    /**
     * On View Created
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set Dialog Title Text View
        dialogTitle.text = title
    }

    /**
     * Initialize Injector
     */
    override fun initializeInjector() {}
}