package com.sanchez.sanchez.bullkeeper_kids.core.permission

import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import com.fernandocejas.arrow.checks.Preconditions
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.sanchez.sanchez.bullkeeper_kids.core.platform.dialogs.NoticeDialogFragment
import java.util.*
import javax.inject.Inject

/**
 * Permission Manager Impl
 */
class PermissionManagerImpl
     @Inject constructor(
        /**
         * App Compat Activity
         */
        private val activity: AppCompatActivity) : IPermissionManager {

    /**
     * Check Permission Listener
     */
    private var checkPermissionListener: IPermissionManager.OnCheckPermissionListener? = null

    /**
     * Set Check Permission Listener
     */
    override fun setCheckPermissionListener(checkPermissionListener: IPermissionManager.OnCheckPermissionListener) {
        this.checkPermissionListener = checkPermissionListener
    }

    /**
     * Should Ask Permission
     * @return
     */
    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * Checks if the androidmanifest.xml contains the given permission.
     * @param permission
     * @return
     */
    private fun appManifestContainsPermission(permission: String): Boolean {

        val pm = activity.packageManager

        try {

            val packageInfo = pm.getPackageInfo(activity.packageName,
                    PackageManager.GET_PERMISSIONS)

            var requestedPermissions: Array<String>? = null
            if (packageInfo != null) {
                requestedPermissions = packageInfo.requestedPermissions
            }
            if (requestedPermissions == null) {
                return false
            }
            if (requestedPermissions.isNotEmpty()) {
                val requestedPermissionsList = Arrays.asList(*requestedPermissions)
                return requestedPermissionsList.contains(permission)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return false
    }


    /**
     * Build Permission Listener
     * @param permission
     * @param reasonText
     * @return
     */
    private fun buildPermissionListener(permission: String, reasonText: String): PermissionListener {

        return object : BasePermissionListener() {

            /**
             * On Permission Granted
             */
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                super.onPermissionGranted(response)
                checkPermissionListener?.onSinglePermissionGranted(permission)
            }

            /**
             * On Permission Denied
             */
            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                super.onPermissionDenied(response)
                NoticeDialogFragment.showDialog(activity,
                        reasonText, object : NoticeDialogFragment.NoticeDialogListener {
                            /**
                             * On Accepted
                             */
                            override fun onAccepted(dialog: DialogFragment) {
                                checkPermissionListener?.onSinglePermissionRejected(permission)

                            }
                        })

            }
        }
    }

    /**
     * Check Single Permission
     * @param permission
     * @param reasonText
     */
    override fun checkSinglePermission(permission: String, reasonText: String) {
        Preconditions.checkNotNull(permission, "Permission can not be null")
        Preconditions.checkNotNull(!permission.isEmpty(), "Permission can not be empty")
        Preconditions.checkNotNull(reasonText, "Reason can not be null")

        if (shouldAskPermission(permission)) {

            val permissionListener = buildPermissionListener(permission, reasonText)

            Dexter.withActivity(activity)
                    .withPermission(permission)
                    .withListener(permissionListener)
                    .check()
        } else {
            checkPermissionListener?.onErrorOccurred(permission)
        }

    }

    /**
     * Should Ask Permission
     * @param permission
     * @return
     */
    override fun shouldAskPermission(permission: String): Boolean {
        if (shouldAskPermission() && appManifestContainsPermission(permission)) {
            val permissionResult = ActivityCompat.checkSelfPermission(activity, permission)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return java.lang.Boolean.TRUE
            }
        }
        return java.lang.Boolean.FALSE
    }
}