package com.galaxytechno.localizedlanguagesample

import android.content.Context
import android.os.Build
import android.os.LocaleList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleHelper(
    @ApplicationContext private val context: Context
) {

    //todo : how to make this initialization of DS with Hilt?
    @Inject
    lateinit var ds: DataStoreSource
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun setCurrentLocale(): Context {
        return updateResources(getLangFromDs())
    }

    private fun setNewLocale(lang: String): Context {
        return updateResources(lang)
    }

    private fun getLangFromDs(): String {
        var defLang = DataStoreSourceImpl.LANG_CN

        applicationScope.launch {
            ds.getLangState().collect {
                defLang = it
            }
        }
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

    fun setLangJapanese() {
        setNewLocale(getLangFromDs())
    }

    fun setLangEnglish() {
        setNewLocale(getLangFromDs())
    }

    fun setLangChina() {
        setNewLocale(getLangFromDs())
    }
}
