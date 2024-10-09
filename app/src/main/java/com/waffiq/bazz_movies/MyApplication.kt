package com.waffiq.bazz_movies

import android.app.Application
import com.waffiq.bazz_movies.core.di.CoreComponent
import com.waffiq.bazz_movies.core.di.DaggerCoreComponent
import com.waffiq.bazz_movies.di.AppComponent
import com.waffiq.bazz_movies.di.DaggerAppComponent

open class MyApplication : Application() {

  private val coreComponent: CoreComponent by lazy {
    DaggerCoreComponent.factory().create(applicationContext)
  }

  val appComponent: AppComponent by lazy {
    DaggerAppComponent.factory().create(coreComponent)
  }
}