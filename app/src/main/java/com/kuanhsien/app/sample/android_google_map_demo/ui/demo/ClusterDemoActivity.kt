/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuanhsien.app.sample.android_google_map_demo.ui.demo

import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.kuanhsien.app.sample.android_google_map_demo.R
import com.kuanhsien.app.sample.android_google_map_demo.data.MapClusterItem
import com.kuanhsien.app.sample.android_google_map_demo.ui.base.BaseDemoActivity
import com.kuanhsien.app.sample.android_google_map_demo.util.MapClusterItemReader
import org.json.JSONException

/**
 * Simple activity demonstrating ClusterManager.
 */
class ClusterDemoActivity : BaseDemoActivity(), GoogleMap.OnCameraIdleListener {
    private var mClusterManager: ClusterManager<MapClusterItem>? = null

    override fun startDemo() {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 10f))

        mClusterManager = ClusterManager(this, googleMap)

        googleMap?.apply {
            setOnCameraIdleListener(this@ClusterDemoActivity)
        }

        try {
            readItems()
        } catch (e: JSONException) {
            Toast.makeText(this, "Reading list of markers error.", Toast.LENGTH_LONG).show()
        }

    }

    @Throws(JSONException::class)
    private fun readItems() {
        val inputStream = resources.openRawResource(R.raw.cluster_demo)
        val items = MapClusterItemReader().read(inputStream)
        mClusterManager?.addItems(items)
    }

    override fun onCameraIdle() {
        refreshCluster()
    }

    private fun refreshCluster() {
        synchronized(this) {
            mClusterManager?.cluster()
        }
    }
}
