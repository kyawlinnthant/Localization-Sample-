package com.galaxytechno.localizedlanguagesample

import android.content.Context
import android.os.Build
import android.os.LocaleList
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LocaleHelper @Inject constructor(
    private val context: Context
) {
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface LocaleHelperEntryPoint {
        fun getDs(): DataStoreSource
    }

    private val ds =
        EntryPointAccessors
            .fromApplication(context, LocaleHelperEntryPoint::class.java)
            .getDs()

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    fun setCurrentLocale(): Context {
        return updateResources(getLangFromDs())
    }

    fun setNewLocale(lang: String): Context {
        return updateResources(lang)
    }

    private fun getLangFromDs(): String {
        var defLang: String = ""
        //todo : this is where we have to access the Ds
        // dataStoreSource, ds

        applicationScope.launch {

            ds.getLangState().collect {
                defLang = it
                Timber.tag("df1").d(it)
                Timber.tag("df2").d(defLang)
                withContext(Dispatchers.Main) {
                    defLang = it
                    updateResources(defLang)
                }
            }

        }

        Timber.tag("df3").d(defLang)
        return defLang
    }

    private fun updateResources(language: String): Context {
        var mContext = context
        val res = mContext.resources
        val configuration = res.configuration
        val locale = Locale(language)

        //our miniSDK = 21 , so we don't need to consider below 17
        //N_MR1 = Api 25
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            //updateConfiguration is deprecated. We have to create new Context and configure resources
            mContext = context.createConfigurationContext(configuration)
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        }
        //N = Api 24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val localeList = LocaleList(locale)
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
        }
        //JELLY_BEAN_MR1 = Api 17
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            res.updateConfiguration(configuration, res.displayMetrics)
            configuration.setLocale(locale)
            Locale.setDefault(locale)
        } else {
            //below Api 17
            res.updateConfiguration(configuration, res.displayMetrics)
            configuration.locale = locale
            Locale.setDefault(locale)
        }

        return mContext
    }
}
