package com.galaxytechno.localizedlanguagesample

import android.content.Context
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
            super.attachBaseContext(LocaleHelper(it).setCurrentLocale())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.lang_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.lang_en -> {
                viewModel.setLang(DataStoreSourceImpl.LANG_EN)
                LocaleHelper(this).setNewLocale(DataStoreSourceImpl.LANG_EN)
                this.recreate()
                return true
            }
            R.id.lang_ja -> {
                viewModel.setLang(DataStoreSourceImpl.LANG_JP)
                LocaleHelper(this).setNewLocale(DataStoreSourceImpl.LANG_JP)
                this.recreate()
                return true
            }
            R.id.lang_zh -> {
                viewModel.setLang(DataStoreSourceImpl.LANG_CN)
                LocaleHelper(this).setNewLocale(DataStoreSourceImpl.LANG_CN)
                this.recreate()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }

        }
    }

}