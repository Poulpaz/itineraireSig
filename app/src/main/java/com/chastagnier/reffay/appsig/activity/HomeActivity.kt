package com.chastagnier.reffay.appsig.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.adapter.ListStationAdapter
import com.chastagnier.reffay.appsig.dataBase.DataBaseHelper
import com.chastagnier.reffay.appsig.model.Station
import java.io.FileOutputStream
import java.io.InputStream

class HomeActivity : AppCompatActivity() {

    lateinit var listAdapter : ListStationAdapter
    lateinit var listStation : List<Station>
    lateinit var DBHelper : DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    private fun copyDatabase() : Boolean {
       /* try {
            var inputStream = this.assets.open(DataBaseHelper.DB_NAME)
            var outFileName = DataBaseHelper.DBLOCATION + DataBaseHelper.DB_NAME
            var outputStream = FileOutputStream(outFileName)
            var buff = ByteArray(1024)
            var length = 0
        }*/
    }
}
