package com.chastagnier.reffay.appsig.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.adapter.ListStationAdapter
import com.chastagnier.reffay.appsig.dataBase.SigDatabase
import com.chastagnier.reffay.appsig.model.GEO_POINT
import com.huma.room_for_asset.RoomAsset
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration



class HomeActivity : AppCompatActivity() {

    lateinit var listAdapterPoint : ListStationAdapter
    lateinit var listAdapterArc : ListStationAdapter
    lateinit var listStation : List<GEO_POINT>
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        listAdapterPoint = ListStationAdapter()
        listAdapterArc = ListStationAdapter()
        rv_point.adapter = listAdapterPoint
        rv_arc.adapter = listAdapterArc

        val dataBase = RoomAsset.databaseBuilder(this, SigDatabase::class.java, "lp_iem_sig.db").allowMainThreadQueries().build()


        dataBase.searchStationDAO().getGeoPoint().subscribe(
                { listAdapterPoint.submitList(it) },
                { Log.e("TESTT", it.toString()) }
        ).dispose()

        dataBase.searchStationDAO().getGeoPoint().subscribe(
                { listAdapterArc.submitList(it) },
                { Log.e("TESTT", it.toString()) }
        ).dispose()

    }
}
