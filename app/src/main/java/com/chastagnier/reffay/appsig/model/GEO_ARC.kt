package com.chastagnier.reffay.appsig.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "GEO_ARC")
data class GEO_ARC(
        @PrimaryKey
        @ColumnInfo(name = "GEO_POI_ID") var id: Int,
        @ColumnInfo(name = "GEO_POI_DEB") var deb: Float?,
        @ColumnInfo(name = "GEO_POI_FIN") var fin: Float?,
        @ColumnInfo(name = "GEO_POI_TEMPS") var temps: String?,
        @ColumnInfo(name = "GEO_POI_DISTANCE") var distance: Int?,
        @ColumnInfo(name = "GEO_POI_SENS") var sens: Int?
)