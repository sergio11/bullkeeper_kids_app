package com.sanchez.sanchez.bullkeeper_kids.core.platform

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.ConfirmationDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment

interface IBasicActivityHandler {

    /**
     * Close Activity
     */
    fun closeActivity()

    /**
     * Show Short Message
     * @param message
     */
     fun showShortMessage(message: String)

    /**
     * Show Short Message
     * @param messageResId
     */
     fun showShortMessage(@StringRes messageResId: Int)

    /**
     * Show Long Message
     * @param message
     */
    fun showLongMessage(message: String)

    /**
     * Show Notice Dialog
     * @param title
     */
    fun showNoticeDialog(title: String)

    /**
     * Show Notice Dialog
     * @param stringResId
     */
    fun showNoticeDialog(stringResId: Int)

    /**
     * Show Notice Dialog
     * @param title
     * @param noticeDialogListener
     */
    fun showNoticeDialog(title: String,
                         noticeDialogListener: NoticeDialogFragment.NoticeDialogListener)

    /**
     * Show Notice Dialog
     * @param stringResId
     * @param noticeDialogListener
     */
    fun showNoticeDialog(stringResId: Int,
                         noticeDialogListener: NoticeDialogFragment.NoticeDialogListener)

    /**
     * Show Progress Dialog
     * @param title
     */
    fun showProgressDialog(title: String)

    /**
     * Show Progress Dialog
     * @param stringResId
     */
    fun showProgressDialog(stringResId: Int)

    /**
     * Hide Progress Dialog
     */
    fun hideProgressDialog()

    /**
     * Show Confirmation Dialog
     * @param title
     */
    fun showConfirmationDialog(title: String)

    /**
     * Show Confirmation Dialog
     * @param stringResId
     */
    fun showConfirmationDialog(stringResId: Int)

    /**
     * Show Confirmation Dialog
     * @param title
     * @param confirmationDialogListener
     */
    fun showConfirmationDialog(title: String, confirmationDialogListener: ConfirmationDialogFragment.ConfirmationDialogListener)


    /**
     * Show Confirmation Dialog
     * @param stringResId
     * @param confirmationDialogListener
     */
    fun showConfirmationDialog(stringResId: Int, confirmationDialogListener: ConfirmationDialogFragment.ConfirmationDialogListener)


    /**
     * Show Simple Snackbar
     * @param actionText
     * @param onClickListener
     */
    fun showLongSimpleSnackbar(viewRoot: ViewGroup, description: String, actionText: String, onClickListener: View.OnClickListener)

    /**
     * Show Simple Snackbar
     * @param actionText
     * @param onClickListener
     */
    fun showLongSimpleSnackbar(viewRoot: ViewGroup, description: String, actionText: String,
                               onClickListener: View.OnClickListener, snackbarCallback: Snackbar.Callback?)

}