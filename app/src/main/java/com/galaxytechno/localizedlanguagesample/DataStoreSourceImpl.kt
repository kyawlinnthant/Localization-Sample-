package com.galaxytechno.localizedlanguagesample

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreSourceImpl(
    private val ds: DataStore<Preferences>
) : DataStoreSource {

    companion object {
        const val LANG_EN = "en"
        const val LANG_JP = "ja"
        const val LANG_CN = "zh"

        val LANG_STATUS = stringPreferencesKey("LANGUAGE_STATUS")
    }

    override suspend fun saveLangState(state: String) {
        ds.edit {
            it[LANG_STATUS] = state
        }
    }

    override suspend fun getLangState(): Flow<String> {
        return ds.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw  exception
            }
            .map {
            it[LANG_STATUS] ?: LANG_JP
        }
    }

}