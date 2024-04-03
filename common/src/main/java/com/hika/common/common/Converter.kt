package com.hika.common.common

fun Int.toRupiahFormat(): String {
    val rupiah = StringBuilder(toString())
    var i = rupiah.length - 3
    while (i > 0) {
        rupiah.insert(i, ".")
        i -= 3
    }
    return "Rp$rupiah"
}