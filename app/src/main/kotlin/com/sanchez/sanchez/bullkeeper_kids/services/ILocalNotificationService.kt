package com.sanchez.sanchez.bullkeeper_kids.services

import android.app.Notification
import android.app.PendingIntent

/**
 * Local Notification Service
 */
interface ILocalNotificationService {

    /**
     * Get Notification
     */
    fun getNotification(contentTitle: String, contentText: String): Notification

    /**
     * Get Notification
     */
    fun getNotification(contentTitle: String, contentText: String, channelId: String?): Notification

    /**
     * Get Notification
     */
    fun getNotification(contentTitle: String, contentText: String, pendingIntent: PendingIntent?): Notification

    /**
     * Get Notification
     */
    fun getNotification(contentTitle: String, contentText: String,
                        channelId: String?, pendingIntent: PendingIntent?): Notification



}