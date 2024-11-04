package com.positiveHelicopter.doobyplus.utility

import com.positiveHelicopter.doobyplus.model.HttpsPart

fun String.findHttpsUrl(): List<HttpsPart> {
    val https = this.trim().split("\\s+".toRegex()).filter {
        it.contains("https://")
    }.map {
        if(!it.startsWith("https://")) {
            val index = it.indexOf("https://")
            it.substring(index)
        } else it
    }
    return https.map {
        HttpsPart(
            url = it,
            startIndex = this.indexOf(it),
            endIndex = this.indexOf(it) + it.length
        )
    }
}