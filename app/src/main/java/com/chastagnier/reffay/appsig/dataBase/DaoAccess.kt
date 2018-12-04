package com.chastagnier.reffay.appsig.dataBase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.chastagnier.reffay.appsig.model.GEO_POINT
import io.reactivex.Single

@Dao
interface DaoAccess {

    @Query("SELECT * FROM GEO_POINT")
    fun getGeoPoint() : Single<List<GEO_POINT>>

    /*@Query("SELECT * FROM GEO_ARC")
    fun getGeoArc() : Single<List<GEO_ARC>>*/
}