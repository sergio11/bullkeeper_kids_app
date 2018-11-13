package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.sanchez.sanchez.bullkeeper_kids.R
import com.sanchez.sanchez.bullkeeper_kids.services.ILocalNotificationService
import javax.inject.Inject

/**
 * Local Notification Service Impl
 */
class LocalNotificationServiceImpl
    @Inject constructor(private val context: Context): ILocalNotificationService {

    init {
        // Create Notification Channel
        createNotificationChannel()
    }

    /**
     * Notification Manager
     */
    private var mNotificationManager: NotificationManager? = null

    /**
     * Create Notification Channel
     */
    private fun createNotificationChannel() {

        // Get System Notification Manager
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.important_notification_channel_title)
            // Create the channel for the notification
            val mChannel = NotificationChannel(context.getString(R.string.important_notification_channel_id)
                    , name, NotificationManager.IMPORTANCE_DEFAULT)
            // Set the Notification Channel for the Notification Manager.
            mNotificationManager!!.createNotificationChannel(mChannel)
        }
    }


    /**
     * Build Basic Notification
     */
    private fun buildBasicNotification(contentTitle: String,
                                   contentText: String): NotificationCompat.Builder {
        /**
         * Build Notification
         */
        return NotificationCompat.Builder(context)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
    }

    /**
     * Get Notification
     */
    override fun getNotification(contentTitle: String,
                                 contentText: String): Notification = getNotification(contentTitle, contentText,
            context.getString(R.string.default_notification_channel_id))

    /**
     * Get Notification
     */
    override fun getNotification(contentTitle: String, contentText: String,
                                 channelId: String?): Notification = getNotification(contentTitle, contentText, channelId, null)


    /**
     * Get Notification
     */
    override fun getNotification(contentTitle: String, contentText: String, pendingIntent: PendingIntent?): Notification
        = getNotification(contentTitle, contentText, context.getString(R.string.default_notification_channel_id), pendingIntent)

    /**
     * Get Notification
     */
    override fun getNotification(contentTitle: String, contentText: String, channelId: String?, pendingIntent: PendingIntent?): Notification {

        val notificationBuilder = buildBasicNotification(contentTitle, contentText)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if(!channelId.isNullOrEmpty())
                notificationBuilder.setChannelId(channelId.orEmpty())

        }

        if(pendingIntent != null)
            notificationBuilder.setContentIntent(pendingIntent)

        return notificationBuilder.build()
    }



}