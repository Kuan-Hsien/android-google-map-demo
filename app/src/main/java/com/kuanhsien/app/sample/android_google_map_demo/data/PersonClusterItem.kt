package com.kuanhsien.app.sample.android_google_map_demo.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class PersonClusterItem(private val mPosition: LatLng, val name: String, val profilePhoto: Int) : ClusterItem {

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getSnippet(): String? {
        return null
    }
}
