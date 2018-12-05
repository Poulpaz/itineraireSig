package com.chastagnier.reffay.appsig

import android.app.Application
import android.content.Context
import com.chastagnier.reffay.appsig.injection.appModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {

    override val kodein = Kodein.lazy {

        bind<Application>() with singleton { this@App }
        bind<Context>() with singleton { instance<Application>() }

        import(appModule)
    }
}