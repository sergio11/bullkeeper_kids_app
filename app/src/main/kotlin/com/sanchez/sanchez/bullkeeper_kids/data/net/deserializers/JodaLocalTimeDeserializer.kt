package com.sanchez.sanchez.bullkeeper_kids.data.net.deserializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.io.IOException

/**
 * Joda Local Time Deserializer
 */
class JodaLocalTimeDeserializer(jodaTimeFormat: String) : JsonDeserializer<LocalTime>() {

    /**
     * Date Time Formatter
     */
    private val fmt: DateTimeFormatter = DateTimeFormat.forPattern(jodaTimeFormat)

    /**
     * Deserialize
     */
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): LocalTime? {
        val textToParse = jp.text.trim { it <= ' ' }
        var localTime: LocalTime? = null
        try {
            localTime = LocalTime.parse(textToParse, fmt)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return localTime
    }
}