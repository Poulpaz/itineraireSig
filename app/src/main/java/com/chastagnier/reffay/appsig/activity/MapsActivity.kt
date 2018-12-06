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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.kodein.di.generic.instance

class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val sigDatabase : SigDatabase by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMinZoomPreference(12.0f);

        // Add a marker in Sydney and move the camera
        val bourgEnBresse = LatLng(46.202781, 5.219243)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bourgEnBresse))

        sigDatabase.searchStationDAO().getGeoPoint().subscribe(
                { addMarker(it) },
                { Log.e("TESTT", it.toString()) }
        ).dispose()
    }

    private fun addMarker(listPoint: List<GEO_POINT>) {
        for (point in listPoint) {
            mMap.addMarker(MarkerOptions()
                    .position(LatLng(point.latitude!!.toDouble(), point.longitude!!.toDouble()))
                    .title(point.nom))
        }

        var lineOptions = PolylineOptions()
        lineOptions.add(LatLng(listPoint.get(28).latitude.toDouble(), listPoint.get(28).longitude.toDouble()))
        lineOptions.add(LatLng(listPoint.get(29).latitude.toDouble(), listPoint.get(29).longitude.toDouble()))
        lineOptions.add(LatLng(listPoint.get(30).latitude.toDouble(), listPoint.get(30).longitude.toDouble()))

        lineOptions.width(10.toFloat());
        lineOptions.color(Color.RED);

        mMap.addPolyline(lineOptions)
    }
}
