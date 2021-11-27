package com.galaxytechno.localizedlanguagesample

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.defaultLang.observe(this) {
            Timber.tag("here").d(it)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let {
            val localeBase = LocaleHelper(it).setCurrentLocale()
            super.attachBaseContext(ContextWrapper(localeBase))
        }
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.let {
            val uiMode = it.uiMode
            it.setTo(baseContext.resources.configuration)
            it.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lang_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.lang_en -> {
                viewModel.setLang(DataStoreSourceImpl.LANG_EN)
//                LocaleHelper(this).setNewLocale(DataStoreSourceImpl.LANG_EN)
            }
            R.id.lang_ja -> {
                viewModel.setLang(DataStoreSourceImpl.LANG_JP)
//                LocaleHelper(this).setNewLocale(DataStoreSourceImpl.LANG_JP)
            }
            R.id.lang_zh -> {
                viewModel.setLang(DataStoreSourceImpl.LANG_CN)
//                LocaleHelper(this).setNewLocale(DataStoreSourceImpl.LANG_CN)
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        this.recreate()
        return true
    }

}