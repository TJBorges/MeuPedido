package com.app.meupedido.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtil {

    private val locale = Locale("pt", "BR")

    fun getCurrentDateTime(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
    }

    fun getTreatedDateTime(dateTime: String): String {
        val date = dateTime.substring(0, 10)
        val currentDate = getCurrentDateTime().substring(0, 10)
        val afternoon = LocalDate.now().minusDays(1)
        val afternoonDate = afternoon.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", locale))

        return when (date) {
            currentDate -> "Hoje, ${dateTime.substring(11, 16)}"
            afternoonDate -> "Ontem, ${dateTime.substring(11, 16)}"
            else -> getFormattedDateOrder(date) + ", ${dateTime.substring(11, 16)}"
        }
    }

    private fun getFormattedDateOrder(date: String): String {
        val localFormatter = SimpleDateFormat("dd/MM/yyyy", locale)
        val localDate = localFormatter.parse(date.trim())
        val dateFormatted = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy", locale))
        val dayWeek = SimpleDateFormat("EEE", locale).format(localDate ?: date)
        val month = SimpleDateFormat("MMMM", locale).format(localDate ?: date)

        val day = dateFormatted.dayOfMonth.toString().lowercase()
        val dayWeekCamelCase = dayWeek.replace(dayWeek.substring(0,1), dayWeek.substring(0,1).uppercase())
        val monthCamelCase = month.replace(month.substring(0,1), month.substring(0,1).uppercase())
        val year = dateFormatted.year.toString()
        return "$dayWeekCamelCase $day $monthCamelCase $year"
    }
}