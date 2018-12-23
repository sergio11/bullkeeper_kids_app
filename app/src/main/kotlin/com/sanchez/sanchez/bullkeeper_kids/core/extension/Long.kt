package com.sanchez.sanchez.bullkeeper_kids.core.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.toDateTimeString(): String{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    return simpleDateFormat.format(calendar.time)
}

/**
 * To Date
 */
fun Long.toDateTime(): Date{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.time
}

