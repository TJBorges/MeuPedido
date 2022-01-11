package com.app.meupedido.model

data class Order(val number: String, val status: String) //, val date: String)

class OrderBuilder (
    var number: String = "",
    var status: String = "",
    var date: String = "" ) {


    //fun build(): Order = Order(number, status)
}

//fun order(block: OrderBuilder.() -> Unit) : Order = OrderBuilder().apply(block).build()
//
//fun orderExample(): MutableList<Order> = mutableListOf(
//    order {
//        number = "EAT5643"
//        status = "em preparação"
//    },
//    order {
//        number = "EAT5643"
//        status = "em preparação"
//    }
//)