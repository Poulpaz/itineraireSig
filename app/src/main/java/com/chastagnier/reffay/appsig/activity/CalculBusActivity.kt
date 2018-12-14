package com.chastagnier.reffay.appsig.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
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
import java.io.File
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
    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

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

    var chaineXml : String = ""
    var xmlFile = "carlosTristanBusStopBenBr.kml"

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

        b_export.setOnClickListener{
            exportKml(actualList)
        }


    }

    private fun calculParcoursEnLargeur(it: Pair<List<GEO_ARC>, List<GEO_POINT>>) {
        //val graph = Graph(it.second, it.first)
        //val PEL = ParcoursEnLargeur(graph)
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

    private fun exportKml(list: LinkedList<GEO_POINT>?) {
        chaineXml += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"

        list?.map {
            chaineXml += getString(it)
        }

        chaineXml += "</kml>"

        File(path, xmlFile).writeText(chaineXml)
        Toast.makeText(this, "Export du KML effectué", Toast.LENGTH_SHORT).show()
    }

    private fun getString(point : GEO_POINT) : String{
        return "<Placemark>\n" +
                "<name>" + point.nom + "</name>" + "\n" +
                "<Point>\n" +
                "<coordinates>" + point.longitude + "," + point.latitude + "</coordinates>\n" +
                "</Point>\n" +
                "</Placemark>\n"
    }
}
