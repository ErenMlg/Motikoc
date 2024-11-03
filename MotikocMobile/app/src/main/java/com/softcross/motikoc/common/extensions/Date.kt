package com.softcross.motikoc.common.extensions

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

fun getCurrentDateTime(): LocalDateTime {
    return LocalDateTime.now()
}

fun compareDates(dateToCompare: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val currentDateTime = getCurrentDateTime()
    val parsedDateToCompare = LocalDateTime.parse(dateToCompare, formatter)
    return currentDateTime.isAfter(parsedDateToCompare)
}

fun stringToLocalDateTime(dateTimeString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(dateTimeString, formatter)
}

fun localDateTimeToMap(dateTime: LocalDateTime): Map<String, Any> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return mapOf("dateTime" to dateTime.format(formatter))
}

fun getCurrentMonth(): Int {
    val currentDate = LocalDate.now()
    val currentMonth = currentDate.monthValue
    return currentMonth
}

fun getCurrentDay(): Int {
    val currentDate = LocalDate.now()
    val dayOfMonth = currentDate.dayOfMonth
    return dayOfMonth
}

fun getCurrentMonthMaxDay(): Int {
    val currentDate = LocalDate.now()
    val currentMonth = currentDate.monthValue
    val maxDaysInMonth = YearMonth.of(currentDate.year, currentMonth).lengthOfMonth()
    return maxDaysInMonth
}

fun getMonthMaxDay(year: Int, month: Int): Int {
    val maxDaysInMonth = YearMonth.of(year, month).lengthOfMonth()
    return maxDaysInMonth
}

fun Int.toMonthString(): String {
    return when (this) {
        1 -> "Ocak"
        2 -> "Şubat"
        3 -> "Mart"
        4 -> "Nisan"
        5 -> "Mayıs"
        6 -> "Haziran"
        7 -> "Temmuz"
        8 -> "Ağustos"
        9 -> "Eylül"
        10 -> "Ekim"
        11 -> "Kasım"
        12 -> "Aralık"
        else -> throw IllegalArgumentException("Geçersiz ay: $this")
    }
}

fun getDayName(month: Int, day: Int, year: Int): String {
    val date = LocalDate.of(year, month, day)
    return when (date.dayOfWeek) {
        java.time.DayOfWeek.MONDAY -> "Pzt"
        java.time.DayOfWeek.TUESDAY -> "Sal"
        java.time.DayOfWeek.WEDNESDAY -> "Çar"
        java.time.DayOfWeek.THURSDAY -> "Per"
        java.time.DayOfWeek.FRIDAY -> "Cum"
        java.time.DayOfWeek.SATURDAY -> "Cmt"
        java.time.DayOfWeek.SUNDAY -> "Paz"
    }
}

fun isDateToday(day: Int, month: Int): Boolean {
    val currentDate = LocalDate.now()
    val inputDate = LocalDate.of(currentDate.year, month, day)
    return currentDate.isEqual(inputDate)
}

fun String.dateTimeToFormattedDate(): String {
    try {
        val inputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
        val date = LocalDate.parse(this, inputFormatter)
        return date.format(outputFormatter)
    } catch (e: Exception) {
        e.printStackTrace()
        return "N/A"
    }
}

fun Long.toDate(format: String = "MM-dd-yyyy", locale: Locale = Locale.getDefault()): String {
    val date = Date(this)
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(date)
}

fun String.stringToLocalDate(pattern: String = "yyyy-MM-dd"): LocalDate {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDate.parse(this, formatter)
}

fun LocalDate.toTurkishDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("tr"))
    return this.format(formatter)
}

fun LocalDateTime.hoursUntil(): Long {
    val localDate = LocalDateTime.now()
    return if (this.isAfter(localDate)) {
        ChronoUnit.HOURS.between(localDate, this)
    } else {
        0
    }
}