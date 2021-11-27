package com.galaxytechno.localizedlanguagesample

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class LocalizedLangApp : Application() {

    @Inject
    lateinit var dataStoreSource: DataStoreSource
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

   /* override fun attachBaseContext(base: Context?) {
        base?.let {
            super.attachBaseContext(LocaleHelper(it).setCurrentLocale())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper(this).setCurrentLocale()
    }*/

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        applicationScope.launch {
            dataStoreSource.getLangState().collect {
                Timber.tag("error").d(it)
            }
        }
    }


}