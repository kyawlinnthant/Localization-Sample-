package com.galaxytechno.localizedlanguagesample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val ds: DataStoreSource
) : ViewModel() {


    private var _defaultLang = MutableLiveData<String>()
    val defaultLang: LiveData<String> get() = _defaultLang

    init {
        getLang()
    }

    private fun getLang() {
        viewModelScope.launch {
            ds.getLangState().collect {
                _defaultLang.postValue(it)
            }
        }
    }
    fun setLang(lang : String){
        viewModelScope.launch {
            ds.saveLangState(lang)
        }
    }

}