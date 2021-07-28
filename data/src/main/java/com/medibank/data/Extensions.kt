package com.medibank.data

fun List<String>.toCsv(): String{
    val b = StringBuilder()
    this.forEach {
        b.append(it).append(",")
    }

    return b.toString()
}