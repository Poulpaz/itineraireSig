package com.chastagnier.reffay.appsig.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.adapter.ListCalculAdapter
import com.chastagnier.reffay.appsig.dataBase.SigDatabase
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.chastagnier.reffay.appsig.model.GEO_POINT
import com.chastagnier.reffay.appsig.model.Graph
import com.chastagnier.reffay.appsig.utils.Dijkstra
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_calcul_points.*


class CalculPointsActivity : BaseActivity(){

    lateinit var listAdapterCalcul: ListCalculAdapter
    var listPoint: List<GEO_POINT> = mutableListOf()
    var listArc: List<GEO_ARC> = mutableListOf()
    val sigDatabase: SigDatabase by instance()
    lateinit var obsListPoint: Observable<List<GEO_POINT>>
    lateinit var obsListArc: Observable<List<GEO_ARC>>
    var idDeb = 0
    var idFin = 0
    lateinit var graph : Graph
    lateinit var dijkstra : Dijkstra

    var actualList : LinkedList<GEO_POINT>? = null

    var pathDijkstra1: LinkedList<GEO_POINT>? = null
    var pathDijkstra2: LinkedList<GEO_POINT>? = null
    var pathDijkstra3: LinkedList<GEO_POINT>? = null
    var pathDijkstra4: LinkedList<GEO_POINT>? = null
    var pathDijkstra5: LinkedList<GEO_POINT>? = null
    var pathDijkstra6: LinkedList<GEO_POINT>? = null
    var pathDijkstra7: LinkedList<GEO_POINT>? = null
    var pathDijkstra21: LinkedList<GEO_POINT>? = null
    var pathDijkstra0: LinkedList<GEO_POINT>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcul_points)

        listAdapterCalcul = ListCalculAdapter()
        rv_calcul_points.adapter = listAdapterCalcul

        sigDatabase.searchStationDAO().getGeoPoint().subscribe(
                {
                    listPoint = it
                    obsListPoint = Observable.just(listPoint)
                },
                { Log.e("TESTT2", it.toString()) }
        ).dispose()

        sigDatabase.searchStationDAO().getGeoArc().subscribe(
                {
                    listArc = it
                    obsListArc = Observable.just(listArc)
                },
                { Log.e("TESTT2", it.toString()) }
        ).dispose()

        Observable.combineLatest(obsListArc, obsListPoint, BiFunction<List<GEO_ARC>?, List<GEO_POINT>?, Pair<List<GEO_ARC>, List<GEO_POINT>>> { l1, l2 -> Pair(l1, l2) })
                .subscribe(
                        {
                            graph = Graph(it.second, it.first)
                            dijkstra = Dijkstra(graph)
                            setSpinnerListener()
                        },
                        { Timber.e(it) }
                )

        b_map_points.setOnClickListener {
            val intent = Intent(this@CalculPointsActivity, MapsActivity::class.java)
            intent.putExtra("listPoint", actualList)
            startActivity(intent)
        }

    }

    private fun calculParcoursEnLargeur(it: Pair<List<GEO_ARC>, List<GEO_POINT>>) {
        val graph = Graph(it.second, it.first)
        val PEL = PEL(graph)
    }

    private fun setSpinnerListener() {

        val adp1 = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listPoint.map { it.nom })
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_points_fin.adapter = adp1
        spinner_points_deb.setAdapter(adp1)

        spinner_points_deb.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, positon: Int, id: Long) {
                idDeb = positon
                calculDijkstra()
            }
        }

        spinner_points_fin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, positon: Int, id: Long) {
                idFin = positon
                calculDijkstra()
            }
        }
    }

    private fun calculDijkstra() {
        dijkstra.reset()
        actualList = dijkstra.getResult(listPoint.get(idDeb), listPoint.get(idFin))
        listAdapterCalcul.submitList(actualList)
        listAdapterCalcul.notifyDataSetChanged()
    }
}