package com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sanchez.sanchez.bullkeeper_kids.R
import kotlinx.android.synthetic.main.notice_dialog_layout.*

/**
 * Notice Fragment
 */
class NoticeDialogFragment: SupportDialogFragment() {

    companion object {

        /**
         * Tag
         */
        val TAG = "NOTICE_DIALOG_FRAGMENT"

        /**
         * Title Arg
         */
        val TITLE_ARG = "DIALOG_TITLE"

        /**
         * Show Dialog
         * @param activity
         * @return
         */
        @JvmStatic
        fun showDialog(activity: AppCompatActivity,
                       title: String, noticeDialogListener: NoticeDialogListener?): NoticeDialogFragment {

            val noticeDialog = NoticeDialogFragment()
            noticeDialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CommonDialogFragmentTheme)
            // Config Arguments
            val args = Bundle()
            args.putString(TITLE_ARG, title)
            noticeDialog.arguments = args
            noticeDialog.isCancelable = false
            // Config Listener
            if (noticeDialogListener != null)
                noticeDialog.setNoticeDialogListener(noticeDialogListener)

            noticeDialog.show(activity.supportFragmentManager, TAG)

            return noticeDialog
        }

        /**
         * Show Dialog
         * @param activity
         * @param title
         * @return
         */
        @JvmStatic
        fun showDialog(activity: AppCompatActivity, title: String): NoticeDialogFragment {
            return showDialog(activity, title, null)
        }

    }

    private var noticeDialogListener: NoticeDialogListener? = null

    /**
     * Set Notice Dialog Listener
     */
    fun setNoticeDialogListener(noticeDialogListener: NoticeDialogListener) {
        this.noticeDialogListener = noticeDialogListener
    }

    /**
     * Get Layout Resource
     */
    override fun getLayoutRes(): Int = R.layout.notice_dialog_layout

    /**
     * Initialize Injector
     */
    override fun initializeInjector() {}

    /**
     * On View Created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set Dialog Title
        dialogTitle.text = title
        // Dismiss On Click Listener
        dismiss.setOnClickListener {
            noticeDialogListener?.onAccepted(this)
            dismiss()
        }
    }

    /**
     * Notice Dialog Listener
     */
    interface NoticeDialogListener {

        /**
         * On Accepted
         * @param dialog
         */
        fun onAccepted(dialog: DialogFragment)
    }
}