package com.chastagnier.reffay.appsig.dataBase

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import com.chastagnier.reffay.appsig.model.Station
import java.util.*

class DataBaseHelper(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    lateinit var dataBase : SQLiteDatabase

    companion object {

        val DBLOCATION = "/data/data/com.chastagnier.reffay.appsig/databases/"

        val DB_NAME = "lp_iem_sig.sqlite"
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private fun openDataBase() {
        var dbBath = context.getDatabasePath(DB_NAME).path
        if(dataBase != null && dataBase.isOpen){
            return
        }
        dataBase = SQLiteDatabase.openDatabase(dbBath,null, SQLiteDatabase.OPEN_READWRITE)
    }

    private fun closeDataBase(){
        if(dataBase != null){
            dataBase.close()
        }
    }

    fun getListStation() : List<Station> {
        var listStation = mutableListOf<Station>()
        openDataBase()
        val cursor = dataBase.rawQuery("SELECT * FROM GEO_POINT", null)
        cursor.moveToFirst()
        while(!cursor.isAfterLast){
            val station = Station(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getString(3), cursor.getDouble(4))
            listStation.add(station)
            cursor.moveToNext()
        }
        cursor.close()
        closeDataBase()
        return listStation
    }

}