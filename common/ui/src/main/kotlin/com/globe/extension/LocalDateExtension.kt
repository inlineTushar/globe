package com.globe.extension

import android.text.format.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*

const val DAY_MONTH_YEAR_PATTERN = "dd MMM yyyy"

fun LocalDate.toDayMonthYearPattern(): String = formatWithPattern(DAY_MONTH_YEAR_PATTERN)

internal fun TemporalAccessor.formatWithPattern(
    pattern: String,
    isCapitalize: Boolean = false
): String =
    DateTimeFormatter
        .ofPattern(safeGetBestDateTimePattern(pattern))
        .format(this)
        .capitalize(isCapitalize)

private fun safeGetBestDateTimePattern(pattern: String): String = DateFormat.getBestDateTimePattern(
    Locale.getDefault(),
    pattern
) ?: pattern

private fun String.capitalize(isCapitalize: Boolean = true) = replaceFirstChar {
    if (isCapitalize && it.isLowerCase()) it.titlecase(Locale.getDefault())
    else it.toString()
}
