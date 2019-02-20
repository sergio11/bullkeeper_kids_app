package com.sanchez.sanchez.bullkeeper_kids.services

import android.app.Notification
import android.app.PendingIntent

/**
 * Local Notification Service
 */
interface ILocalNotificationService {

    /**
     * Get Notification
     * @param contentTitle
     * @param contentText
     */
    fun getNotification(contentTitle: String, contentText: String): Notification

    /**
     * Get Notification
     * @param contentTitle
     * @param contentText
     * @param channelId
     */
    fun getNotification(contentTitle: String, contentText: String, channelId: String?): Notification

    /**
     * Get Notification
     * @param contentTitle
     * @param contentText
     * @param pendingIntent
     */
    fun getNotification(contentTitle: String, contentText: String, pendingIntent: PendingIntent?): Notification

    /**
     * Get Notification
     * @param contentTitle
     * @param contentText
     * @param channelId
     * @param pendingIntent
     */
    fun getNotification(contentTitle: String, contentText: String,
                        channelId: String?, pendingIntent: PendingIntent?): Notification


    /**
     * Send Notification
     * @param notificationId
     * @param contentTitle
     * @param contentText
     */
    fun sendNotification(notificationId: Int, contentTitle: String, contentText: String)

    /**
     * Send Notification
     * @param notificationId
     * @param contentTitle
     * @param contentText
     * @param channelId
     */
    fun sendNotification(notificationId: Int, contentTitle: String, contentText: String, channelId: String?)

    /**
     * Send Notification
     * @param notificationId
     * @param contentTitle
     * @param contentText
     * @param pendingIntent
     */
    fun sendNotification(notificationId: Int, contentTitle: String, contentText: String, pendingIntent: PendingIntent?)

    /**
     * Send Notification
     * @param notificationId
     * @param contentTitle
     * @param contentText
     * @param channelId
     * @param pendingIntent
     */
    fun sendNotification(notificationId: Int, contentTitle: String, contentText: String,
                        channelId: String?, pendingIntent: PendingIntent?)



}