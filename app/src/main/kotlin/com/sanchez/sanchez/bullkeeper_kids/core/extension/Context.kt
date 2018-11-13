package com.sanchez.sanchez.bullkeeper_kids.core.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.annotation.StringRes
import android.widget.Toast

val Context.networkInfo: NetworkInfo? get() =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

/**
 * Show Short Message
 */
fun Context.showShortMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


/**
 * Show Short Message
 */
fun Context.showShortMessage(@StringRes messageResId: Int) =
        showShortMessage(getString(messageResId))


/**
 * Show Long Message
 * @param message
 */
fun Context.showLongMessage(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()