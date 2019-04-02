package com.sanchez.sanchez.bullkeeper_kids.services

import android.app.Notification
import android.app.PendingIntent

/**
 * Local Notification Service
 */
interface ILocalNotificationService {

    /**
     * Get Notification
     * @param type
     * @param contentTitle
     * @param contentText
     */
    fun getNotification(type: NotificationTypeEnum, contentTitle: String, contentText: String): Notification

    /**
     * Get Notification
     * @param type
     * @param contentTitle
     * @param contentText
     * @param channelId
     */
    fun getNotification(type: NotificationTypeEnum, contentTitle: String, contentText: String, channelId: String?): Notification

    /**
     * Get Notification
     * @param type
     * @param contentTitle
     * @param contentText
     * @param pendingIntent
     */
    fun getNotification(type: NotificationTypeEnum, contentTitle: String, contentText: String, pendingIntent: PendingIntent?): Notification

    /**
     * Get Notification
     * @parma type
     * @param contentTitle
     * @param contentText
     * @param channelId
     * @param pendingIntent
     */
    fun getNotification(type: NotificationTypeEnum, contentTitle: String, contentText: String,
                        channelId: String?, pendingIntent: PendingIntent?): Notification


    /**
     * Send Notification
     * @param type
     * @param notificationId
     * @param contentTitle
     * @param contentText
     */
    fun sendNotification(type: NotificationTypeEnum, notificationId: Int, contentTitle: String, contentText: String)

    /**
     * Send Notification
     * @param type
     * @param notificationId
     * @param contentTitle
     * @param contentText
     * @param channelId
     */
    fun sendNotification(type: NotificationTypeEnum, notificationId: Int, contentTitle: String, contentText: String, channelId: String?)

    /**
     * Send Notification
     * @param type
     * @param notificationId
     * @param contentTitle
     * @param contentText
     * @param pendingIntent
     */
    fun sendNotification(type: NotificationTypeEnum, notificationId: Int, contentTitle: String, contentText: String, pendingIntent: PendingIntent?)

    /**
     * Send Notification
     * @param type
     * @param notificationId
     * @param contentTitle
     * @param contentText
     * @param channelId
     * @param pendingIntent
     */
    fun sendNotification(type: NotificationTypeEnum, notificationId: Int, contentTitle: String, contentText: String,
                        channelId: String?, pendingIntent: PendingIntent?)



    enum class NotificationTypeEnum {
        IMPORTANT, NORMAL
    }


}