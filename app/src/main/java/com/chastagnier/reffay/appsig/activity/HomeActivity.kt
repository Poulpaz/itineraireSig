package com.chastagnier.reffay.appsig.activity

import android.os.Bundle
import android.util.Log
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.adapter.ListPointAdapter
import com.chastagnier.reffay.appsig.dataBase.SigDatabase
import com.chastagnier.reffay.appsig.model.GEO_POINT
import kotlinx.android.synthetic.main.activity_home.*
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.chastagnier.reffay.appsig.adapter.ListArcAdapter
import org.kodein.di.generic.instance


class HomeActivity() : BaseActivity() {

    lateinit var listAdapterPoint : ListPointAdapter
    lateinit var listAdapterArc : ListArcAdapter
    lateinit var listStation : List<GEO_POINT>
    val sigDatabase : SigDatabase by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //sigDatabase = RoomAsset.databaseBuilder(this, SigDatabase::class.java, "lp_iem_sig.db").addMigrations(MIGRATION_1_2).fallbackToDestructiveMigration().allowMainThreadQueries().build()

        listAdapterPoint = ListPointAdapter()
        listAdapterArc = ListArcAdapter()
        rv_point.adapter = listAdapterPoint
        rv_arc.adapter = listAdapterArc

        sigDatabase.searchStationDAO().getGeoPoint().subscribe(
                { listAdapterPoint.submitList(it)
                    Log.e("TESTT", it.toString())},
                { Log.e("TESTT2", it.toString()) }
        ).dispose()

        sigDatabase.searchStationDAO().getGeoArc().subscribe(
                { listAdapterArc.submitList(it)
                    Log.e("TESTT", it.toString())},
                { Log.e("TESTT2", it.toString()) }
        ).dispose()

    }

    //création du menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //ajoute les entrées de menu_test à l'ActionBar
        menuInflater.inflate(R.menu.menu_map, menu)
        return true
    }


    //gère le click sur une action de l'ActionBar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent
        when (item.itemId) {
            R.id.action_map -> {
                intent = Intent(this@HomeActivity, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
