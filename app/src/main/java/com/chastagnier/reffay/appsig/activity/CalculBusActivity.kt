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
import kotlinx.android.synthetic.main.activity_calcul_bus.*
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.*

class CalculBusActivity : BaseActivity(){

    lateinit var listAdapterCalcul: ListCalculAdapter
    var listPoint: List<GEO_POINT> = mutableListOf()
    var listArc: List<GEO_ARC> = mutableListOf()
    val sigDatabase: SigDatabase by instance()
    lateinit var obsListPoint: Observable<List<GEO_POINT>>
    lateinit var obsListArc: Observable<List<GEO_ARC>>
    var idBus = 0
    var idMethod = 0

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
        setContentView(R.layout.activity_calcul_bus)

        listAdapterCalcul = ListCalculAdapter()
        rv_calcul.adapter = listAdapterCalcul

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
                            calculDijkstra(it)
                            calculParcoursEnLargeur(it)
                            setSpinnerListener()
                        },
                        { Timber.e(it) }
                )

        b_map.setOnClickListener {
            val intent = Intent(this@CalculBusActivity, MapsActivity::class.java)
            intent.putExtra("listPoint", actualList)
            startActivity(intent)
        }


    }

    private fun calculParcoursEnLargeur(it: Pair<List<GEO_ARC>, List<GEO_POINT>>) {
        val graph = Graph(it.second, it.first)
        val PEL = PEL(graph)
        PEL.execute(it.second.first())
    }

    private fun setSpinnerListener() {
        spinner_bus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, positon: Int, id: Long) {
                idBus = positon
                setList()
            }
        }

        spinner_method.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, positon: Int, id: Long) {
                idMethod = positon
                setList()
            }
        }
    }

    private fun setList(){
        actualList = when(idBus){
            0 -> pathDijkstra1
            1 -> pathDijkstra2
            2 -> pathDijkstra3
            3 -> pathDijkstra4
            4 -> pathDijkstra5
            5 -> pathDijkstra6
            6 -> pathDijkstra7
            7 -> pathDijkstra21
            8 -> pathDijkstra0
            else -> null
        }
        listAdapterCalcul.submitList(actualList)
        listAdapterCalcul.notifyDataSetChanged()
    }

    private fun calculDijkstra(it: Pair<List<GEO_ARC>, List<GEO_POINT>>) {
        val graph = Graph(it.second, it.first)
        val dijkstra = Dijkstra(graph)
        pathDijkstra1 = dijkstra.getResult(listPoint.get(0), listPoint.get(27))
        dijkstra.reset()
        pathDijkstra2 = dijkstra.getResult(listPoint.get(28), listPoint.get(52))
        dijkstra.reset()
        pathDijkstra3 = dijkstra.getResult(listPoint.get(53), listPoint.get(82))
        dijkstra.reset()
        pathDijkstra4 = dijkstra.getResult(listPoint.get(83), listPoint.get(110))
        dijkstra.reset()
        pathDijkstra5 = dijkstra.getResult(listPoint.get(111), listPoint.get(154))
        dijkstra.reset()
        pathDijkstra6 = dijkstra.getResult(listPoint.get(155), listPoint.get(185))
        dijkstra.reset()
        pathDijkstra7 = dijkstra.getResult(listPoint.get(186), listPoint.get(210))
        dijkstra.reset()
        pathDijkstra21 = dijkstra.getResult(listPoint.get(211), listPoint.get(218))
        dijkstra.reset()
        pathDijkstra0 = dijkstra.getResult(listPoint.get(219), listPoint.get(224))
    }
}
