package com.app.meupedido.util

import androidx.core.text.isDigitsOnly

class ValidateInsertOrder {

    private val nameStore = NameStore()

    fun validateNumberOrder(number: String): Boolean {
        return (number.isNotEmpty()
                && number.length > 6 && number.length < 9
                && validateFormat(number)
                && (nameStore.name(number.substring(0, 3)).isNotEmpty()))
    }

    private fun validateFormat(number: String): Boolean {
        val idStore = number.substring(0, 3)
        val number = number.substring(3, 7)
        return (!idStore.isDigitsOnly() && number.isDigitsOnly())
    }
}