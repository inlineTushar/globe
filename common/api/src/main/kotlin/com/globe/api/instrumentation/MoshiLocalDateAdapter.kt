package com.globe.api.instrumentation

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.globe.api.extension.toLocalDateTime
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import kotlin.jvm.Throws

class MoshiLocalDateAdapter : JsonAdapter<LocalDate>() {

    companion object {
        var fmt: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .appendOptional(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
            .toFormatter()
    }

    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): LocalDate {
        val stringDate = reader.nextString()

        return LocalDate.parse(stringDate, fmt)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: LocalDate?) {
        val long =
            value?.toLocalDateTime()?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
                ?: throw JsonDataException("Invalid Local Date: $value")
        writer.value(long)
    }
}
