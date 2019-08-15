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

package com.kuanhsien.app.sample.android_google_map_demo.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.kuanhsien.app.sample.android_google_map_demo.R


abstract class BaseDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    protected var googleMap: GoogleMap? = null
        private set

    protected val layoutId: Int
        get() = R.layout.activity_maps

    open fun getSaveValueOfLayoutId() = layoutId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getSaveValueOfLayoutId())
        setUpMap()
    }

    override fun onResume() {
        super.onResume()
        setUpMap()
    }

    override fun onMapReady(map: GoogleMap) {
        if (this.googleMap != null) {
            return
        }
        this.googleMap = map
        startDemo()
    }

    private fun setUpMap() {
        (supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment).getMapAsync(this)
    }

    /**
     * Run the demo-specific code.
     */
    protected abstract fun startDemo()
}
