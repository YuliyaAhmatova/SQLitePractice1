package com.example.sqlitepractice1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_NAME = "PRODUCT_DATABASE"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "product_table"
        val KEY_NAME = "name"
        val KEY_WEIGHT = "weight"
        val KEY_COST = "cost"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val PRODUCT_TABLE = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_NAME + " TEXT PRIMARY KEY, " +
                KEY_WEIGHT + " TEXT," +
                KEY_COST + " TEXT" + ")")
        db?.execSQL(PRODUCT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, product.productName)
        contentValues.put(KEY_WEIGHT, product.productWeight)
        contentValues.put(KEY_COST, product.productCost)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun readProduct(): MutableList<Product> {
        val productList: MutableList<Product> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return productList
        }
        var productName: String
        var productWeight: String
        var productCost: String
        if (cursor.moveToFirst()) {
            do {
                productName = cursor.getString(cursor.getColumnIndex("name"))
                productWeight = cursor.getString(cursor.getColumnIndex("weight"))
                productCost = cursor.getString(cursor.getColumnIndex("cost"))
                val product = Product(productName, productWeight, productCost)
                productList.add(product)
            } while (cursor.moveToNext())
        }
        return productList
    }

    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, product.productName)
        contentValues.put(KEY_WEIGHT, product.productWeight)
        contentValues.put(KEY_COST, product.productCost)
        db.update(TABLE_NAME, contentValues, "name=?", arrayOf(product.productName))
        db.close()
    }

    fun deleteProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, product.productName)
        db.delete(TABLE_NAME, "name=?", arrayOf(product.productName))
        db.close()
    }
}