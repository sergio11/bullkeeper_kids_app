package com.sanchez.sanchez.bullkeeper_kids.core.extension

import org.joda.time.LocalDateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.lang.Exception


fun String.Companion.empty() = ""

/**
 * To Local Time
 */
fun String.toLocalTime(dateFormat: String): LocalTime? {
    val fmt: DateTimeFormatter = DateTimeFormat.forPattern(dateFormat)
    var localTime: LocalTime? = null
    try {
        localTime = LocalTime.parse(this, fmt)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return localTime
}

/**
 * To Int Array
 */
fun String.toIntArray(): IntArray? {
    var result: IntArray? = null
    try {
        result = this.split(",")
                .map { it.toInt() }
                .toIntArray()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return result
}

