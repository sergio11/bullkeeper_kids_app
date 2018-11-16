package com.sanchez.sanchez.bullkeeper_kids.core.permission

/**
 * Permission Manager
 */
interface IPermissionManager {

    /**
     * Check Single Permission
     * @param permission
     * @param reasonText
     */
     fun checkSinglePermission(permission: String, reasonText: String)

    /**
     * Should Ask Permission
     * @param permission
     * @return
     */
     fun shouldAskPermission(permission: String): Boolean

    /**
     * Set Check Permission Listener
     * @param checkPermissionListener
     */
    fun setCheckPermissionListener(checkPermissionListener: OnCheckPermissionListener)


    /**
     * On Check Permission Listener
     */
    interface OnCheckPermissionListener {
        fun onSinglePermissionGranted(permission: String)
        fun onSinglePermissionRejected(permission: String)
        fun onErrorOccurred(permission: String)
    }

}