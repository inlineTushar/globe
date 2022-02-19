package com.globe.api.extension

import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDate.toLocalDateTime(): LocalDateTime =
    this.atStartOfDay()
