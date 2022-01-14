package com.app.meupedido.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderTable")
data class Order (
    @PrimaryKey(autoGenerate = false)
    val number: String,
    val status: String,
    val date: String,
    val nameStore: String,
    val icon: String
)