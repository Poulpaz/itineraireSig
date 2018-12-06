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
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.chastagnier.reffay.appsig.model.Graph
import com.chastagnier.reffay.appsig.utils.Dijkstra
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import org.kodein.di.generic.instance
import timber.log.Timber




class HomeActivity() : BaseActivity() {

    lateinit var listAdapterPoint: ListPointAdapter
    lateinit var listAdapterArc: ListArcAdapter
    var listPoint: List<GEO_POINT> = mutableListOf()
    var listArc: List<GEO_ARC> = mutableListOf()
    val sigDatabase: SigDatabase by instance()
    lateinit var obsListPoint: Observable<List<GEO_POINT>>
    lateinit var obsListArc: Observable<List<GEO_ARC>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

        Observable.combineLatest(obsListArc, obsListPoint, BiFunction<List<GEO_ARC>?, List<GEO_POINT>?, Pair<List<GEO_ARC>, List<GEO_POINT>>> { l1, l2 -> Pair(l1, l2) })
                .subscribe(
                        {
                            Log.d("TESTT1", it.first.toString())
                            Log.d("TESTT2", it.second.toString())
                            calculDijkstra(it)
                        },
                        { Timber.e(it) }
                )

    }

    private fun calculDijkstra(it: Pair<List<GEO_ARC>, List<GEO_POINT>>) {
        val graph = Graph(it.second, it.first)
        val dijkstra = Dijkstra(graph)
        val path = dijkstra.getResult(listPoint.get(28), listPoint.get(52))

        path?.forEach {
            Log.d("TESTTPATH", it.toString())
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
                intent = Intent(this@HomeActivity, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
