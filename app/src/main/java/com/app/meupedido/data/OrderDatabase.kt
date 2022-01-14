package com.app.meupedido.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Order::class, Archived::class], version = 1, exportSchema = false)
abstract class OrderDatabase: RoomDatabase() {

    abstract fun orderDao(): OrderDao
    abstract fun archivedDao(): ArchivedDao

    companion object {
        @Volatile
        private var INSTANCE: OrderDatabase? = null

        fun getDatabase(context: Context): OrderDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrderDatabase::class.java,
                    "orderDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}