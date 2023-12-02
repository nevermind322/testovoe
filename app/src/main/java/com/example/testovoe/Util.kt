package com.example.testovoe

import java.lang.IllegalStateException
import java.util.Calendar
import java.util.Date

fun getDateString(date: Date): String {
    val calendar = Calendar.getInstance()
    calendar.time = date
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH).getMonthName()
    val year = calendar.get(Calendar.YEAR)
    return "$dayOfMonth $month, $year"
}

fun Int.getMonthName() = when (this) {
    Calendar.JANUARY -> "января"
    Calendar.FEBRUARY -> "февраля"
    Calendar.MARCH -> "марта"
    Calendar.APRIL -> "апреля"
    Calendar.MAY -> "мая"
    Calendar.JUNE -> "июня"
    Calendar.JULY -> "июля"
    Calendar.AUGUST -> "августа"
    Calendar.SEPTEMBER -> "сентября"
    Calendar.OCTOBER -> "октября"
    Calendar.NOVEMBER -> "ноября"
    Calendar.DECEMBER -> "декабря"
    else -> throw IllegalStateException()
}

fun Double.formatAsAmount() = "%.${2}f".format(this)