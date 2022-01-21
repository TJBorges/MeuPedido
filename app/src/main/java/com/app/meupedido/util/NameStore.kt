package com.app.meupedido.util

class NameStore() {

    fun name(cod: String): String {
        return when (cod) {
            "BOB" -> return "Bob's"
            "BKG" -> return "Burger King"
            "GRF" -> return "Girafas"
            "HAB" -> return "Habib's"
            "KFC" -> return "KFC"
            "MCD" -> return "McDonald's"
            "PZH" -> return "Pizza Hut"
            "SPL" -> return "Spoleto"
            "STB" -> return "Starbucks"
            "SBW" -> return "Subway"
            "VVC" -> return "Viveda do Camarão"
            else -> ""

        }
    }
}