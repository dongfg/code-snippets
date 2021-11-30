package com.dongfg.jackson_datetime

import java.time.LocalDateTime
import java.time.OffsetDateTime

data class DateTimeRequest(
    val localDateTime: LocalDateTime,
    val offsetDateTime: OffsetDateTime,
)

data class DateTimeResponse(
    val localDateTime: LocalDateTime,
    val offsetDateTime: OffsetDateTime,
)