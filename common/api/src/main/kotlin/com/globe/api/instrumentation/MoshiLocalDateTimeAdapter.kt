package com.globe.api.instrumentation

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import kotlin.jvm.Throws

class MoshiLocalDateTimeAdapter : JsonAdapter<LocalDateTime>() {

    companion object {
        var fmt: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ISO_DATE_TIME)
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .appendOptional(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter()
    }

    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): LocalDateTime {
        val stringDate = reader.nextString()
        return LocalDateTime.parse(stringDate, fmt)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        // Empty
    }
}
