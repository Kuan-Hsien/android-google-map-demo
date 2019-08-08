package com.kuanhsien.app.sample.android_google_map_demo.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.kuanhsien.app.sample.android_google_map_demo.util.emptyString

class MapClusterItem(
    private val mPosition: LatLng,  // LatLng(lat, lng)
    private var mTitle: String = emptyString(),
    private var mSnippet: String = emptyString()

): ClusterItem {

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }
}