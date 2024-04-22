package com.hika.common.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Int.toRupiahFormat(): String {
    val rupiah = StringBuilder(toString())
    var i = rupiah.length - 3
    while (i > 0) {
        rupiah.insert(i, ".")
        i -= 3
    }
    return "Rp$rupiah"
}

fun Date.toOrderHistoryFormat(): String {
    val date = this.toString()
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.ENGLISH)

    val result = inputFormat.parse(date) ?: this

    return outputFormat.format(result)
}