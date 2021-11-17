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
                LocaleHelper(this).setLangEnglish()
                this.recreate()
                return true
            }
            R.id.lang_ja -> {
                LocaleHelper(this).setLangJapanese()
                this.recreate()
                return true
            }
            R.id.lang_zh -> {
                LocaleHelper(this).setLangChina()
                this.recreate()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

}