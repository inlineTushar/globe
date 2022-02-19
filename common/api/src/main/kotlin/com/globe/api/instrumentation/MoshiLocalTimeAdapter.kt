package com.globe.api.instrumentation

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MoshiLocalTimeAdapter : JsonAdapter<LocalTime>() {

    companion object {
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_TIME
    }

    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): LocalTime {
        val stringDate = reader.nextString()
        return LocalTime.parse(stringDate, dateFormatter)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: LocalTime?) {
    }
}
