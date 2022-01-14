package com.app.meupedido.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "archivedTable")
data class Archived (
    @PrimaryKey(autoGenerate = false)
    val number: String,
    val status: String,
    val date: String,
    val nameStore: String,
    val icon: String
)