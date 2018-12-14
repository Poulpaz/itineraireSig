package com.chastagnier.reffay.appsig.activity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Half.toFloat
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
import android.widget.Toast
import com.chastagnier.reffay.appsig.model.Point
import kotlinx.android.synthetic.main.activity_calcul_bus.*
import kotlinx.android.synthetic.main.activity_calcul_points.*
import java.io.File
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.sin


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
    var bigListArc = ArrayList<GEO_ARC>()
    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    var chaineXml : String = ""
    var xmlFile = "carlosTristanBusStopBenBr.kml"

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
                            it.first.forEach {
                                bigListArc.add(it)
                            }
                            getReversedArc(it.first, it.second)
                            graph = Graph(it.second, bigListArc)
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

        b_export_points.setOnClickListener{
            exportKml(actualList)
        }

    }

    private fun getReversedArc(listArc: List<GEO_ARC>, listPoint: List<GEO_POINT>){
        listArc.forEach {arc ->
            bigListArc.add(GEO_ARC(arc.id, arc.fin, arc.deb, arc.temps, arc.distance, arc.sens))
            bigListArc.map{bigArc ->
                bigArc.distance = calculL(
                        listPoint.find { it.id == bigArc.deb }, listPoint.find { it.id == bigArc.fin }
                )
            }
        }
    }

 /*   private fun calculateKms(geoPoints: List<GEO_POINT>) {
        var result = 0.0
        val i = 0
        for (geoPoint in geoPoints) {
            val sum = ArrayList<Double>()
            sum.addAll(calculL(lat((geoPoints[i].latitude.toDouble())), lon((geoPoints[i].longitude.toDouble()))))
            sum.addAll(calculL(lat((geoPoints[i + 1].latitude.toDouble())), lon((geoPoints[i + 1].longitude.toDouble()))))
            result += Math.sqrt(Math.pow((sum[i] - sum[i + 2]), 2.0) + Math.pow((sum[i + 1] - sum[i + 3]), 2.0))
        }
    }
*/
    private fun lat(lat: Double): Double {
        return lat / 180 * Math.PI
    }

    private fun lon(lon: Double): Double {
        return lon / 180 * Math.PI
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

    private fun calculParcoursEnLargeur(it: Pair<List<GEO_ARC>, List<GEO_POINT>>) {
        //val graph = Graph(it.second, it.first)
        //val PEL = PEL(graph)
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

    private fun exportKml(list: LinkedList<GEO_POINT>?) {
        chaineXml += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"

        list?.map {
            chaineXml += getString(it)
        }

        chaineXml += "</kml>"

        File(path, xmlFile).writeText(chaineXml)
        Toast.makeText(this, "Export du KML effectué dans votre dossier Download", Toast.LENGTH_SHORT).show()
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