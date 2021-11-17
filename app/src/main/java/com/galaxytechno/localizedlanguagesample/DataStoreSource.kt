package com.galaxytechno.localizedlanguagesample

import kotlinx.coroutines.flow.Flow

interface DataStoreSource {

    suspend fun saveLangState(state: String)
    suspend fun getLangState(): Flow<String>
}