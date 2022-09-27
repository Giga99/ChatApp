package com.medium.client.common.ui

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private val DATE_TIME_FORMATTER_DATE =
    DateTimeFormatter.ofPattern("MM/dd/yyyy").withZone(ZoneId.systemDefault())

private val DATE_TIME_FORMATTER_TIME =
    DateTimeFormatter.ofPattern("h:mm a").withZone(ZoneId.systemDefault())

fun Instant.toPrettierDateFormat(): String =
    if (this.atZone(ZoneId.systemDefault()).toLocalDate().isEqual(LocalDate.now()))
        DATE_TIME_FORMATTER_TIME.format(this)
    else
        DATE_TIME_FORMATTER_DATE.format(this)