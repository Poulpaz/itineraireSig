package com.chastagnier.reffay.appsig.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.dataBase.SigDatabase
import com.chastagnier.reffay.appsig.model.GEO_POINT

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.kodein.di.generic.instance

class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val sigDatabase : SigDatabase by instance()
    var listPath = mutableListOf<PolylineOptions>()
    lateinit var listFromCalcul : List<GEO_POINT>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        listFromCalcul = intent.getSerializableExtra("listPoint") as List<GEO_POINT>
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(12.0f);

        val bourgEnBresse = LatLng(46.202781, 5.219243)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bourgEnBresse))

        sigDatabase.searchStationDAO().getGeoPoint().subscribe(
                {
                    Log.d("TESTDJ", it.toString())
                    drawPath(it)
                    addMarker(it) },
                { Log.e("TESTT", it.toString()) }
        ).dispose()
    }

    private fun drawPath(listPoint: List<GEO_POINT>) {

        var path = listPoint.first().partition
        var lineOptions = PolylineOptions()
        lineOptions.color(Color.RED)
        lineOptions.width(10.toFloat());

        if(listFromCalcul != null){
            listFromCalcul.forEach {
                lineOptions.add(LatLng(it.latitude.toDouble(), it.longitude.toDouble()))
            }
        }else {

            listPoint.forEach {
                if (path == it.partition) {
                    lineOptions.add(LatLng(it.latitude.toDouble(), it.longitude.toDouble()))
                } else {
                    path = it.partition
                    listPath.add(lineOptions)
                    lineOptions = PolylineOptions()
                    lineOptions.color(Color.RED)
                    lineOptions.width(10.toFloat());
                    lineOptions.add(LatLng(it.latitude.toDouble(), it.longitude.toDouble()))
                }
            }
        }

        listPath.add(lineOptions)
        Log.d("TESTPATH", listPath.toString())
    }

    private fun addMarker(listPoint: List<GEO_POINT>) {

        if(listFromCalcul != null){
            for (point in listFromCalcul) {
                mMap.addMarker(MarkerOptions()
                        .position(LatLng(point.latitude!!.toDouble(), point.longitude!!.toDouble()))
                        .title(point.nom))
            }
        }else {
            for (point in listPoint) {
                mMap.addMarker(MarkerOptions()
                        .position(LatLng(point.latitude!!.toDouble(), point.longitude!!.toDouble()))
                        .title(point.nom))
            }
        }

        listPath.forEach {
            mMap.addPolyline(it)
        }
    }
}
