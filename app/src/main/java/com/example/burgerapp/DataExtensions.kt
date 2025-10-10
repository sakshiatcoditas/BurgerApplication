package com.example.burgerapp


import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormattedDate(pattern: String = "dd MMM yyyy, hh:mm a", locale: Locale = Locale.getDefault()): String {
    return SimpleDateFormat(pattern, locale).format(Date(this))
}
