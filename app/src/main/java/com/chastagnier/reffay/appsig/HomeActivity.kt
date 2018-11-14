package com.chastagnier.reffay.appsig

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.MapView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
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
