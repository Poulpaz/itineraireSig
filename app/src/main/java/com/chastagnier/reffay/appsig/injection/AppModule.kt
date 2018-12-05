package com.chastagnier.reffay.appsig.injection

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration
import com.chastagnier.reffay.appsig.dataBase.SigDatabase
import com.huma.room_for_asset.RoomAsset
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val appModule = Kodein.Module("AppModule") {

    bind<SigDatabase>() with singleton {
        RoomAsset.databaseBuilder(instance(), SigDatabase::class.java, "lp_iem_sig.db").fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }

}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Since we didn't alter the table, there's nothing else to do here.
    }
}