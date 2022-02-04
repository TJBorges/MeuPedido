package com.app.meupedido.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {

    fun getCurrentDateTime(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
    }

    fun getTreatedDateTime(dateTime: String): String {
        val date = dateTime.substring(0, 10)
        val currentDate = getCurrentDateTime().substring(0, 10)
        val affternnon = LocalDate.now().minusDays(1)
        val affternoonDate = affternnon.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        return if (date == currentDate)
            "Hoje, ${dateTime.substring(11, 19)}"
        else if (date == affternoonDate)
            "Ontem, ${dateTime.substring(11, 19)}"
        else dateTime
    }
}