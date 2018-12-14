package com.chastagnier.reffay.appsig.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.adapter.ListPointAdapter
import com.chastagnier.reffay.appsig.dataBase.SigDatabase
import com.chastagnier.reffay.appsig.model.GEO_POINT
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import com.chastagnier.reffay.appsig.adapter.ListArcAdapter
import com.chastagnier.reffay.appsig.model.GEO_ARC
import io.reactivex.Observable
import org.kodein.di.generic.instance


class HomeActivity() : BaseActivity() {

    lateinit var listAdapterPoint: ListPointAdapter
    lateinit var listAdapterArc: ListArcAdapter
    var listPoint: List<GEO_POINT> = mutableListOf()
    var listArc: List<GEO_ARC> = mutableListOf()
    val sigDatabase: SigDatabase by instance()
    lateinit var obsListPoint: Observable<List<GEO_POINT>>
    lateinit var obsListArc: Observable<List<GEO_ARC>>

    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupPermissions()
        //sigDatabase = RoomAsset.databaseBuilder(this, SigDatabase::class.java, "lp_iem_sig.db").addMigrations(MIGRATION_1_2).fallbackToDestructiveMigration().allowMainThreadQueries().build()

        listAdapterPoint = ListPointAdapter()
        listAdapterArc = ListArcAdapter()
        rv_point.adapter = listAdapterPoint
        rv_arc.adapter = listAdapterArc

        sigDatabase.searchStationDAO().getGeoPoint().subscribe(
                {
                    listAdapterPoint.submitList(it)
                    listPoint = it
                    obsListPoint = Observable.just(listPoint)
                },
                { Log.e("TESTT2", it.toString()) }
        ).dispose()

        sigDatabase.searchStationDAO().getGeoArc().subscribe(
                {
                    listAdapterArc.submitList(it)
                    listArc = it
                    obsListArc = Observable.just(listArc)
                },
                { Log.e("TESTT2", it.toString()) }
        ).dispose()

        b_calcul_bus.setOnClickListener {
            intent = Intent(this@HomeActivity, CalculBusActivity::class.java)
            startActivity(intent)
        }

        b_calcul_points.setOnClickListener {
            intent = Intent(this@HomeActivity, CalculPointsActivity::class.java)
            startActivity(intent)
        }

    }

    //création du menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //ajoute les entrées de menu_test à l'ActionBar
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }


    //gère le click sur une action de l'ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var intent: Intent
        when (item.itemId) {
            R.id.action_map -> {
                val list = ArrayList<GEO_POINT>()
                intent = Intent(this@HomeActivity, MapsActivity::class.java)
                intent.putExtra("listPoint", list)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("tag", "Permission to record denied")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access the external storage is required for this app to write on your storage.")
                        .setTitle("Permission required")

                builder.setPositiveButton("OK"
                ) { dialog, id ->
                    Log.i("tag", "Clicked")
                    makeRequest()
                }

                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RECORD_REQUEST_CODE)
    }
}
