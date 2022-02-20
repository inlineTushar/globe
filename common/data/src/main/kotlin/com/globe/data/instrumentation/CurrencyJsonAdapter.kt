package com.globe.data.instrumentation

import com.globe.data.extension.ifLet
import com.globe.data.model.Currency
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException

class CurrencyJsonAdapter : JsonAdapter<List<Currency>>() {

    companion object {
        private const val KEY_CURRENCY_NAME = "name"
        private const val KEY_CURRENCY_SYMBOL = "symbol"
    }

    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): List<Currency> {
        val currencies: MutableList<Currency> = mutableListOf()
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
                        currencies.add(Currency(currencyCode, name, symbol))
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return currencies
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: List<Currency>?) {
        // Empty
    }
}