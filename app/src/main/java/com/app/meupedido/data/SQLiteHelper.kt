//package com.app.meupedido.data
//
//import android.annotation.SuppressLint
//import android.content.ContentValues
//import android.content.Context
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import com.app.meupedido.model.Order
//import java.lang.Exception
//
//class SQLiteHelper(context: Context) :
//    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
//
//    companion object {
//        private const val DATABASE_NAME = 1
//        private const val DATABASE_VERSION = "myOrder.db"
//        private const val  TB_ORDER = "tb_order"
//        private const val  NUMBER = "number"
//        private const val  STATUS = "status"
//        private const val  DATE = "date"
//    }
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        val createTbOrder = ("CREATE TABLE " + TB_ORDER + "("
//                + NUMBER + "TEXT PRIMARY KEY, "
//                + STATUS + "TEXT, "
//                + DATE + "TEXT" + ")")
//        db?.execSQL(createTbOrder)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
//        db!!.execSQL("DROP TABLE IF EXISTS $TB_ORDER")
//        onCreate(db)
//    }
//
//    fun insertOrder(order: Order) : Long {
//        val db = this.writableDatabase
//
//        val contentValues = ContentValues()
//        contentValues.put(NUMBER, order.number)
//        contentValues.put(STATUS, order.status)
//        contentValues.put(DATE, order.date)
//
//        val sucess = db.insert(TB_ORDER, null, contentValues)
//        db.close()
//        return sucess
//    }
//
//    @SuppressLint("Range")
//    fun getAllOrders() : ArrayList<Order> {
//        val orderList: ArrayList<Order> = ArrayList()
//        val selectQuery = "SELECT * FROM $TB_ORDER"
//        val db = this.readableDatabase
//
//        val cursor : Cursor?
//
//        try {
//            cursor = db.rawQuery(selectQuery, null)
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//            db.execSQL(selectQuery)
//            return ArrayList()
//        }
//
//        var number: String
//        var status: String
//        var date: String
//
//        if (cursor.moveToFirst()) {
//            do {
//                number = cursor.getString(cursor.getColumnIndex("number"))
//                status = cursor.getString(cursor.getColumnIndex("status"))
//                date = cursor.getString(cursor.getColumnIndex("date"))
//
//                val order = Order(number = number, status = status, date = date)
//                orderList.add(order)
//            } while (cursor.moveToNext())
//        }
//        return orderList
//    }
//}