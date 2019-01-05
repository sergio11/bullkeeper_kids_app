package com.sanchez.sanchez.bullkeeper_kids.core.extension

import java.text.SimpleDateFormat
import java.util.*


/**
 * To String Format
 */
fun Date.toStringFormat(dateFormat: String): String {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    return sdf.format(this)
}


