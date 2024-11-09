package com.positiveHelicopter.doobyplus.utility

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

fun String.convertIsoToDDMMMYYYYHHmm(): String {
    try {
        val isoFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
        val date = LocalDateTime.parse(this, isoFormatter)
        return date.format(formatter)
    } catch (e: Exception) {
        return this
    }
}

fun String.convertDurationToHHmm(): String {
    var result = ""
    for (i in this) {
        result += when(i) {
            in '0'..'9' -> i
            's' -> break
            else -> ':'
        }
    }
    return result
}

fun String.convertTweetDateToDDMMMYYYYHHmm(): String {
    try {
        val formatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("MMMM dd, yyyy 'at' hh:mma")
            .toFormatter()
            .withZone(ZoneId.of("+08:00"))
        val date = ZonedDateTime.parse(this.trim(), formatter)
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
            .withZone(ZoneId.systemDefault()))
    } catch (e: Exception) {
        e.printStackTrace()
        return this
    }
}