package com.chastagnier.reffay.appsig.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "GEO_POINT")
data class GEO_POINT(
        @PrimaryKey
        @ColumnInfo(name = "GEO_POI_ID") var id: Int,
        @ColumnInfo(name = "GEO_POI_LATITUDE") var latitude: Float,
        @ColumnInfo(name = "GEO_POI_LONGITUDE") var longitude: Float,
        @ColumnInfo(name = "GEO_POI_NOM") var nom: String,
        @ColumnInfo(name = "GEO_POI_PARTITION") var partition: Int
) : Serializable