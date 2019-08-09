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

package com.kuanhsien.app.sample.android_google_map_demo.ui.map

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.kuanhsien.app.sample.android_google_map_demo.R
import com.kuanhsien.app.sample.android_google_map_demo.data.PersonClusterItem
import com.kuanhsien.app.sample.android_google_map_demo.ui.common.BaseDemoActivity
import com.kuanhsien.app.sample.android_google_map_demo.ui.common.TAG_DEMO
import com.kuanhsien.app.sample.android_google_map_demo.util.MultiDrawable
import java.util.*
import kotlin.math.min


/**
 * Demonstrates heavy customisation of the look of rendered clusters.
 */
class ClusterCustomMarkerDemoActivity : BaseDemoActivity(),
        ClusterManager.OnClusterClickListener<PersonClusterItem>,
        ClusterManager.OnClusterInfoWindowClickListener<PersonClusterItem>,
        ClusterManager.OnClusterItemClickListener<PersonClusterItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<PersonClusterItem> {

    private val tag = this.javaClass.simpleName
    private var mClusterManager: ClusterManager<PersonClusterItem>? = null
    private val mRandom = Random(1984)
    private val dataList =
        listOf(
            // http://www.flickr.com/photos/sdasmarchives/5036248203/
            PersonClusterItem(position(), "Walter", R.drawable.walter),

            // http://www.flickr.com/photos/usnationalarchives/4726917149/
            PersonClusterItem(position(), "Gran", R.drawable.gran),

            // http://www.flickr.com/photos/nypl/3111525394/
            PersonClusterItem(position(), "Ruth", R.drawable.ruth),

            // http://www.flickr.com/photos/smithsonian/2887433330/
            PersonClusterItem(position(), "Stefan", R.drawable.stefan),

            // http://www.flickr.com/photos/library_of_congress/2179915182/
            PersonClusterItem(position(), "Mechanic", R.drawable.mechanic),

            // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
            PersonClusterItem(position(), "Yeats", R.drawable.yeats),

            // http://www.flickr.com/photos/sdasmarchives/5036231225/
            PersonClusterItem(position(), "John", R.drawable.john),

            // http://www.flickr.com/photos/anmm_thecommons/7694202096/
            PersonClusterItem(position(), "Trevor the Turtle", R.drawable.turtle),

            // http://www.flickr.com/photos/usnationalarchives/4726892651/
            PersonClusterItem(position(), "Teach", R.drawable.teacher)
        )

    override fun onClusterClick(cluster: Cluster<PersonClusterItem>): Boolean {

        Log.d(TAG_DEMO, "[$tag] onClusterClick")

        // Show a toast with some info when the cluster is clicked.
        val firstName = cluster.items.iterator().next().name
        Toast.makeText(this, cluster.size.toString() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show()

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        val builder = LatLngBounds.builder()
        for (item in cluster.items) {
            builder.include(item.position)
        }
        // Get the LatLngBounds
        val bounds = builder.build()

        // Animate camera to the bounds
        try {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    override fun onClusterInfoWindowClick(cluster: Cluster<PersonClusterItem>) {

        // Does nothing, but you could go to a list of the users.
        Log.d(TAG_DEMO, "[$tag] onClusterInfoWindowClick")
        Toast.makeText(this, "onClusterInfoWindowClick" , Toast.LENGTH_SHORT).show()
    }

    override fun onClusterItemClick(item: PersonClusterItem): Boolean {

        // Does nothing, but you could go into the user's profile page, for example.
        Log.d(TAG_DEMO, "[$tag] onClusterItemClick")
        Toast.makeText(this, "onClusterItemClick" , Toast.LENGTH_SHORT).show()

        return false
    }

    override fun onClusterItemInfoWindowClick(item: PersonClusterItem) {

        // Does nothing, but you could go into the user's profile page, for example.
        Log.d(TAG_DEMO, "[$tag] onClusterItemInfoWindowClick")
        Toast.makeText(this, "onClusterItemInfoWindowClick" , Toast.LENGTH_SHORT).show()
    }

    override fun startDemo() {
        mClusterManager = ClusterManager(this, googleMap)

        googleMap?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(51.503186, -0.126446), 9.5f))

            setOnCameraIdleListener(mClusterManager)
            setOnMarkerClickListener(mClusterManager)
            setOnInfoWindowClickListener(mClusterManager)
        }

        mClusterManager?.run {
            renderer = PersonRenderer()

            setOnClusterClickListener(this@ClusterCustomMarkerDemoActivity)
            setOnClusterItemClickListener(this@ClusterCustomMarkerDemoActivity)
            setOnClusterInfoWindowClickListener(this@ClusterCustomMarkerDemoActivity)
            setOnClusterItemInfoWindowClickListener(this@ClusterCustomMarkerDemoActivity)

            addItems(dataList)
            cluster()
        }
    }

    // return LatLng object
    private fun position(): LatLng {
        return LatLng(random(51.6723432, 51.38494009999999), random(0.148271, -0.3514683))
    }

    // return random position ( * 0.x and add an offset let random value in a limited scale)
    private fun random(min: Double, max: Double): Double {
        return mRandom.nextDouble() * (max - min) + min
    }

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private inner class PersonRenderer :
        DefaultClusterRenderer<PersonClusterItem>(applicationContext, googleMap, mClusterManager) {
        private val mIconGenerator = IconGenerator(applicationContext)
        private val mClusterIconGenerator = IconGenerator(applicationContext)
        private val mImageView = ImageView(applicationContext)
        private val mClusterImageView: ImageView
        private val mDimension: Int

        init {
            val multiProfile = layoutInflater.inflate(R.layout.multi_profile, null)
            mClusterIconGenerator.setContentView(multiProfile)
            mClusterImageView = multiProfile.findViewById(R.id.image)

            mDimension = resources.getDimension(R.dimen.custom_profile_image).toInt()
            val padding = resources.getDimension(R.dimen.custom_profile_padding).toInt()

            mImageView.layoutParams = ViewGroup.LayoutParams(mDimension, mDimension)
            mImageView.setPadding(padding, padding, padding, padding)

            mIconGenerator.setContentView(mImageView)
        }

        override fun onBeforeClusterItemRendered(personClusterItem: PersonClusterItem?, markerOptions: MarkerOptions?) {
            // Draw a single person (PersonClusterItem).
            // Set the info window to show their name.
            personClusterItem?.let { clusterItem ->

                mImageView.setImageResource(clusterItem.profilePhoto)
                val icon = mIconGenerator.makeIcon()

                markerOptions?.run {
                    icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .title(clusterItem.name)
                }
            }
        }

        override fun onBeforeClusterRendered(cluster: Cluster<PersonClusterItem>, markerOptions: MarkerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            val profilePhotos = ArrayList<Drawable>(min(4, cluster.size))
            val width = mDimension
            val height = mDimension

            for (p in cluster.items) {
                // Draw 4 at most.
                if (profilePhotos.size == 4)
                    break

                ContextCompat.getDrawable(this@ClusterCustomMarkerDemoActivity, p.profilePhoto)?.let { drawable ->
                    drawable.setBounds(0, 0, width, height)
                    profilePhotos.add(drawable)
                }
            }

            val multiDrawable = MultiDrawable(profilePhotos)
            multiDrawable.setBounds(0, 0, width, height)

            mClusterImageView.setImageDrawable(multiDrawable)
            val icon = mClusterIconGenerator.makeIcon(cluster.size.toString())
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
        }

        override fun shouldRenderAsCluster(cluster: Cluster<PersonClusterItem>): Boolean {
            // Always render clusters.

            return cluster.size > 1
        }
    }
}
