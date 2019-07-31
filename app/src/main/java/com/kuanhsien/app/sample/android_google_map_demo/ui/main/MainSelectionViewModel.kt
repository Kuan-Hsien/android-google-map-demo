package com.kuanhsien.app.sample.android_google_map_demo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuanhsien.app.sample.android_google_map_demo.data.DemoType

class MainSelectionViewModel : ViewModel() {

    private val dataList = DemoType.getDemoTypeList()
    val demoTypeListLiveData = MutableLiveData<List<DemoType>>()

    fun prepareData() {
        demoTypeListLiveData.postValue(dataList)
    }
}