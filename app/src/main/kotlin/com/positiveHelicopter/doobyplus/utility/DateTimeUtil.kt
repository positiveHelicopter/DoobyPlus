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
    if (this.isEmpty()) return this
    try {
        var result = ""
        val splits = this.split("h", "m", "s", ".").filter {
            it.isNotEmpty()
        }.toMutableList()
        if (this.contains(".")) splits.removeAt(splits.lastIndex)
        splits.forEachIndexed { index, s ->
            if (s.isEmpty()) return@forEachIndexed
            result += if (s.length == 1 && index != 0) "0$s" else s
            if (index < splits.size - 1) result += ":"
        }
        if (splits.size > 1) return result
        result = if (result.length != 1) "0:$result" else "0:0$result"
        return result
    } catch (e: Exception) {
        return this
    }
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