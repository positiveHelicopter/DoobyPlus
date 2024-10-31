package com.positiveHelicopter.doobyplus.utility

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.convertIsoToDDMMYYYYHHmm(): String {
    try {
        val isoFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
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