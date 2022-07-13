package com.notes.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


fun currentLocalDateTime(): LocalDateTime = convertToLocalDateTime(Calendar.getInstance().time)

fun convertToLocalDateTime(dateToConvert: Date): LocalDateTime {
    return dateToConvert.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}