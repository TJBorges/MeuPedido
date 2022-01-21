package com.app.meupedido.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {

    fun getCurrentDateTime(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
    }
}