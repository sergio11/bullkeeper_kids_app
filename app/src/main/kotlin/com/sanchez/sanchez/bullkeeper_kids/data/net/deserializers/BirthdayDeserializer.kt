package com.sanchez.sanchez.bullkeeper_kids.data.net.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Birth Day Deserializer
 */
class BirthdayDeserializer
    constructor(private val dateFormat: String): JsonDeserializer<Date>() {

    override fun deserialize(jsonparser: JsonParser?, ctxt: DeserializationContext?): Date {
        val format: SimpleDateFormat  = SimpleDateFormat(dateFormat, Locale.getDefault())
        try {
            return format.parse(jsonparser?.text)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }
}