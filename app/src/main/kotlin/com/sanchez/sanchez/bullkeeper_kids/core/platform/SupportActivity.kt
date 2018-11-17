package com.sanchez.sanchez.bullkeeper_kids.core.platform

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fernandocejas.arrow.checks.Preconditions
import com.sanchez.sanchez.bullkeeper_kids.core.permission.IPermissionManager
import com.sanchez.sanchez.bullkeeper_kids.core.permission.PermissionManagerImpl
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.ConfirmationDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.ProgressDialogFragment
import com.sanchez.sanchez.bullkeeper_kids.presentation.broadcast.MonitoringDeviceAdminReceiver
import timber.log.Timber

/**
 * Support Activity
 */
abstract class SupportActivity: AppCompatActivity(),
        IBasicActivityHandler, IPermissionManager.OnCheckPermissionListener {

    /**
     * Permission Manager
     */
    lateinit var permissionManager: IPermissionManager

    /**
     * On Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManagerImpl(this)
        permissionManager.setCheckPermissionListener(this)
    }

    /**
     * Show Short Message
     * @param message
     */
    override fun showShortMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Show Short Message
     * @param messageResId
     */
    override fun showShortMessage(@StringRes messageResId: Int) {
        showShortMessage(getString(messageResId))
    }

    /**
     * Show Long Message
     * @param message
     */
    override fun showLongMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Dismiss all DialogFragments added to given FragmentManager and child fragments
     */
    private fun dismissAllDialogs(fragmentManager: FragmentManager) {

        val fragments = fragmentManager.fragments

        if (!fragments.isEmpty()) {
            for (fragment in fragments) {
                if (fragment is DialogFragment) {
                    fragment.dismissAllowingStateLoss()
                }

                val childFragmentManager = fragment.childFragmentManager
                dismissAllDialogs(childFragmentManager)
            }

        }

    }

    /**
     * Show Notice Dialog
     * @param title
     */
    override fun showNoticeDialog(title: String) {
        dismissAllDialogs(supportFragmentManager)
        NoticeDialogFragment.showDialog(this, title)
    }

    /**
     * Show Notice Dialog
     * @param stringResId
     */
    override fun showNoticeDialog(stringResId: Int) {
        showNoticeDialog(getString(stringResId))
    }

    /**
     * Show Notice Dialog
     * @param title
     * @param noticeDialogListener
     */
    override fun showNoticeDialog(title: String,
                         noticeDialogListener: NoticeDialogFragment.NoticeDialogListener) {
        dismissAllDialogs(supportFragmentManager)
        NoticeDialogFragment.showDialog(this, title, noticeDialogListener)
    }

    /**
     * Show Notice Dialog
     * @param stringResId
     * @param noticeDialogListener
     */
    override fun showNoticeDialog(stringResId: Int,
                         noticeDialogListener: NoticeDialogFragment.NoticeDialogListener) {
        showNoticeDialog(getString(stringResId), noticeDialogListener)
    }

    /**
     * Show Progress Dialog
     * @param title
     */
    override fun showProgressDialog(title: String) {
        dismissAllDialogs(supportFragmentManager)
        ProgressDialogFragment.showDialog(this, title)
    }

    /**
     * Show Progress Dialog
     * @param stringResId
     */
    override fun showProgressDialog(stringResId: Int) {
        showProgressDialog(getString(stringResId))
    }

    /**
     * Hide Progress Dialog
     */
    override fun hideProgressDialog() {
        ProgressDialogFragment.cancelCurrent()
    }

    /**
     * Show Confirmation Dialog
     * @param title
     */
    override fun showConfirmationDialog(title: String) {
        dismissAllDialogs(supportFragmentManager)
        ConfirmationDialogFragment.showDialog(this, title)
    }

    /**
     * Show Confirmation Dialog
     * @param stringResId
     */
    override fun showConfirmationDialog(stringResId: Int) {
        showConfirmationDialog(getString(stringResId))
    }

    /**
     * Show Confirmation Dialog
     * @param title
     * @param confirmationDialogListener
     */
    override fun showConfirmationDialog(title: String, confirmationDialogListener: ConfirmationDialogFragment.ConfirmationDialogListener) {
        dismissAllDialogs(supportFragmentManager)
        ConfirmationDialogFragment.showDialog(this, title, confirmationDialogListener)
    }

    /**
     * Show Confirmation Dialog
     * @param stringResId
     * @param confirmationDialogListener
     */
    override fun showConfirmationDialog(stringResId: Int, confirmationDialogListener: ConfirmationDialogFragment.ConfirmationDialogListener) {
        showConfirmationDialog(getString(stringResId), confirmationDialogListener)
    }

    /**
     * On Single Permission Granted
     * @param permission
     */
    override fun onSinglePermissionGranted(permission: String) {
        Timber.d("On Single Permission Granted: %s", permission)
    }

    /**
     * On Single Permission Rejected
     * @param permission
     */
    override fun onSinglePermissionRejected(permission: String) {
        Timber.d("On Single Permission Rejected: %s", permission)
    }

    /**
     * On Error Ocurred
     */
    override fun onErrorOccurred(permission: String) {
        Timber.d("On Error Ocurred: %s", permission)
    }

    /**
     * Show Long Simple Snackbar
     * @param viewRoot
     * @param description
     * @param actionText
     * @param onClickListener
     */
    override fun showLongSimpleSnackbar(viewRoot: ViewGroup, description: String, actionText: String,
                               onClickListener: View.OnClickListener) {
        showLongSimpleSnackbar(viewRoot, description, actionText, onClickListener, null)
    }

    /**
     * Show Simple Snackbar
     * @param actionText
     * @param onClickListener
     */
    override fun showLongSimpleSnackbar(viewRoot: ViewGroup, description: String, actionText: String,
                                        onClickListener: View.OnClickListener, snackbarCallback: Snackbar.Callback?) {
        Preconditions.checkNotNull(viewRoot, "View Root can not be null")
        Preconditions.checkNotNull(actionText, "Action Text can not be null")
        Preconditions.checkNotNull(onClickListener, "Click Listener can not be null")

        val snackbar = Snackbar.make(viewRoot, description, Snackbar.LENGTH_LONG)

        snackbar.setAction(actionText, onClickListener)
        if (snackbarCallback != null) {
            snackbar.addCallback(snackbarCallback)
        }

        snackbar.show()
    }

    /**
     * Close Activity
     */
    override fun closeActivity() = finish()

    /**
     * Is Device Policy Manager Activty
     */
    override fun isDevicePolicyManagerActive(): Boolean {
        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return devicePolicyManager
                .isAdminActive(MonitoringDeviceAdminReceiver.getComponentName(this))
    }
}