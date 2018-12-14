package com.chastagnier.reffay.appsig.activity

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import kotlinx.android.synthetic.main.activity_calcul_points.*
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
    var idDeb = 0
    var idFin = 0
    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    lateinit var bigListArc : ArrayList<GEO_ARC>

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
                            distance.text = "La distance est " +calculL(listPoint.get(idDeb), listPoint.get(idFin)).toString()+" mètres"
                            setSpinnerListener()
                        },
                        { Timber.e(it) }
                )


    }

    private fun getReversedArc(list: List<GEO_ARC>){
        list.forEach {
            bigListArc.add(GEO_ARC(it.id, it.fin, it.deb, it.temps, it.distance, it.sens))
        }
    }

    private fun calculParcoursEnLargeur(it: Pair<List<GEO_ARC>, List<GEO_POINT>>) {
        //val graph = Graph(it.second, it.first)
        //val PEL = ParcoursEnLargeur(graph)
    }

    private fun setSpinnerListener() {

        val adp1 = ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listPoint.map { it.nom })
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_bus.adapter = adp1
        spinner_method.setAdapter(adp1)

        spinner_bus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, positon: Int, id: Long) {
                idDeb = positon
                distance.text = "La distance est " +calculL(listPoint.get(idDeb), listPoint.get(idFin)).toString()+" mètres"
            }
        }

        spinner_method.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, positon: Int, id: Long) {
                idFin = positon
                distance.text = "La distance est " +calculL(listPoint.get(idDeb), listPoint.get(idFin)).toString()+" mètres"
            }
        }
    }

    private fun calculL(point1 : GEO_POINT?, point2 : GEO_POINT?): Float {

        val n = 0.7289686274
        val C = 11745793.39
        val e = 0.08248325676
        val Xs = 600000
        val Ys = 8199695.768

        var gamma0 = 3600 * 2 + 60 * 20 + 14.025;
        gamma0 = (gamma0 / (180 * 3600)) * Math.PI;
        var lat1 = (point1!!.latitude * 3600).toDouble()
        var lon1 = (point1!!.longitude * 3600).toDouble()
        lat1 = (lat1 / (180 * 3600)) * Math.PI
        lon1 = (lon1 / (180.0 * 3600.0)) * Math.PI

        var lat2 = (point2!!.latitude * 3600).toDouble()
        var lon2 = (point2!!.longitude * 3600).toDouble()
        lat2 = (lat2 / (180 * 3600)) * Math.PI
        lon2 = (lon2 / (180.0 * 3600.0)) * Math.PI

        val L1 =
                0.5 * Math.log((1 + Math.sin(lat1)) / (1 - Math.sin(lat1))) -
                        (e / 2) * Math.log((1 + e * Math.sin(lat1)) / (1 - e * Math.sin(lat1)));
        val R1 = C * Math.exp(-n * L1);
        val gamma1 = n * (lon1 - gamma0);

        val L2 =
                0.5 * Math.log((1 + Math.sin(lat2)) / (1 - Math.sin(lat2))) -
                        (e / 2) * Math.log((1 + e * Math.sin(lat2)) / (1 - e * Math.sin(lat2)));
        val R2 = C * Math.exp(-n * L2);
        val gamma2 = n * (lon2 - gamma0);

        val Lx1 = Xs + R1 * Math.sin(gamma1);
        val Ly1 = Ys - R1 * Math.cos(gamma1);

        val Lx2 = Xs + R2 * Math.sin(gamma2);
        val Ly2 = Ys - R2 * Math.cos(gamma2);

        val distance = Math.sqrt(Math.pow(Lx2 - Lx1, 2.0) + Math.pow(Ly2 - Ly1, 2.0));

        return distance.toFloat()
    }

    private fun setList(){
        actualList = when(idDeb){
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

    private fun calculDijkstra(points: List<GEO_POINT>, arcs : List<GEO_ARC>) {
        val graph = Graph(points, arcs)
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
