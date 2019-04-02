package com.sanchez.sanchez.bullkeeper_kids.services.impl

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
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

    // Get default ringtone
    private val alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

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
        val notificationBuilder =  NotificationCompat.Builder(context)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setSound(alertSound)
                .setWhen(System.currentTimeMillis())

        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(LongArray(0))

        return notificationBuilder

    }

    /**
     * Build Important Notification
     */
    private fun buildImportantNotification(contentTitle: String,
                                       contentText: String): NotificationCompat.Builder {
        /**
         * Build Notification
         */
        val notificationBuilder = NotificationCompat.Builder(context)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setSound(alertSound)
                .setWhen(System.currentTimeMillis())

        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(LongArray(0))

        return notificationBuilder
    }


    /**
     * Vibrate
     * @param milliseconds
     */
    private fun vibrate(milliseconds: Long) {
        val vibratorService = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibratorService?.vibrate(milliseconds)
    }

    /**
     * Get Notification
     */
    override fun getNotification(type: ILocalNotificationService.NotificationTypeEnum, contentTitle: String,
                                 contentText: String): Notification = getNotification(type, contentTitle, contentText,
            context.getString(R.string.default_notification_channel_id))

    /**
     * Get Notification
     */
    override fun getNotification(type: ILocalNotificationService.NotificationTypeEnum, contentTitle: String, contentText: String,
                                 channelId: String?): Notification = getNotification(type, contentTitle, contentText, channelId, null)


    /**
     * Get Notification
     */
    override fun getNotification(type: ILocalNotificationService.NotificationTypeEnum, contentTitle: String, contentText: String, pendingIntent: PendingIntent?): Notification
        = getNotification(type, contentTitle, contentText, context.getString(R.string.default_notification_channel_id), pendingIntent)

    /**
     * Get Notification
     */
    override fun getNotification(type: ILocalNotificationService.NotificationTypeEnum, contentTitle: String, contentText: String, channelId: String?, pendingIntent: PendingIntent?): Notification {
        val notificationBuilder = if(type == ILocalNotificationService.NotificationTypeEnum.IMPORTANT)
            buildImportantNotification(contentTitle, contentText)
        else
            buildBasicNotification(contentTitle, contentText)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            if(!channelId.isNullOrEmpty())
                notificationBuilder.setChannelId(channelId.orEmpty())
        if(pendingIntent != null)
            notificationBuilder.setContentIntent(pendingIntent)
        return notificationBuilder.build()
    }

    /**
     *
     */
    override fun sendNotification(type: ILocalNotificationService.NotificationTypeEnum, notificationId: Int, contentTitle: String, contentText: String) {
        vibrate(1000)
        mNotificationManager?.notify(notificationId,
                getNotification(type, contentTitle, contentText))
    }

    /**
     *
     */
    override fun sendNotification(type: ILocalNotificationService.NotificationTypeEnum, notificationId: Int, contentTitle: String, contentText: String, channelId: String?) {
        vibrate(1000)
        mNotificationManager?.notify(notificationId,
                getNotification(type, contentTitle, contentText, channelId))
    }

    /**
     * Send Notification
     * @param notificationId
     * @param contentTitle
     * @param contentText
     * @param pendingIntent
     */
    override fun sendNotification(type: ILocalNotificationService.NotificationTypeEnum, notificationId: Int, contentTitle: String, contentText: String, pendingIntent: PendingIntent?) {
        vibrate(1000)
        mNotificationManager?.notify(notificationId,
                getNotification(type, contentTitle, contentText, pendingIntent))
    }

    /**
     * Send Notification
     * @param notificationId
     * @param contentTitle
     * @param contentText
     * @param channelId
     * @param pendingIntent
     */
    override fun sendNotification(type: ILocalNotificationService.NotificationTypeEnum, notificationId: Int, contentTitle: String, contentText: String, channelId: String?, pendingIntent: PendingIntent?) {
        vibrate(1000)
        mNotificationManager?.notify(notificationId,
                getNotification(type, contentTitle, contentText, channelId, pendingIntent))
    }

}