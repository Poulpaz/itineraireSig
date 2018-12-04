package com.chastagnier.reffay.appsig.dataBase

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.chastagnier.reffay.appsig.model.GEO_POINT

@Database(entities = [GEO_POINT::class, GEO_ARC::class], version = 2, exportSchema = false)

abstract class SigDatabase : RoomDatabase() {
    abstract fun searchStationDAO(): DaoAccess
}