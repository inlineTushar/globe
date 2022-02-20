package com.globe.data.datasource.remote.instrumentation

import com.globe.data.extension.ifLet
import com.globe.data.model.CurrencyApiModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException

class CurrencyJsonAdapter : JsonAdapter<List<CurrencyApiModel>>() {

    companion object {
        private const val KEY_CURRENCY_NAME = "name"
        private const val KEY_CURRENCY_SYMBOL = "symbol"
    }

    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): List<CurrencyApiModel> {
        val currencyApiModels: MutableList<CurrencyApiModel> = mutableListOf()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.peek()) {
                JsonReader.Token.NAME -> {
                    val currencyCode = reader.nextName()
                    reader.beginObject()
                    var currencyName: String? = null
                    var currencySymbol: String? = null
                    while (reader.hasNext()) {
                        when (reader.nextName()) {
                            KEY_CURRENCY_NAME -> currencyName = reader.nextString()
                            KEY_CURRENCY_SYMBOL -> currencySymbol = reader.nextString()
                            else -> reader.skipValue()
                        }
                    }
                    ifLet(currencyName, currencySymbol) { name, symbol ->
                        currencyApiModels.add(CurrencyApiModel(currencyCode, name, symbol))
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return currencyApiModels
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: List<CurrencyApiModel>?) {
        // Empty
    }
}