package com.sanchez.sanchez.bullkeeper_kids.core.platform

import android.content.Context
import com.sanchez.sanchez.bullkeeper_kids.core.extension.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Injectable class which returns information about the network connection state.
 */
@Singleton
class NetworkHandler
@Inject constructor(private val context: Context) {
    val isConnected get() = context.networkInfo?.isConnectedOrConnecting
}