package com.kuanhsien.app.sample.android_google_map_demo.data

import androidx.appcompat.app.AppCompatActivity
import com.kuanhsien.app.sample.android_google_map_demo.R
import com.kuanhsien.app.sample.android_google_map_demo.ui.demo.*

enum class DemoType(val titleRes: Int, val activityClass: Class<out AppCompatActivity>) {

    MarkerDemo(R.string.title_activity_marker_demo, MarkerDemoActivity::class.java),
    MarkerRetapCloseInfoDemo(R.string.title_activity_marker_retap_close_info_demo, MarkerRetapCloseInfoDemoActivity::class.java),
    ClusterDemo(R.string.title_activity_cluster_demo, ClusterDemoActivity::class.java),
    ClusterCustomMarkerDemo(R.string.title_activity_cluster_custom_marker_demo, ClusterCustomMarkerDemoActivity::class.java),
    IconGeneratorDemo(R.string.title_activity_icon_generator_demo, IconGeneratorDemoActivity::class.java),
    PolygonDemo(R.string.title_activity_polygon_demo, PolygonDemoActivity::class.java)
    ;

    companion object {
        fun getDemoTypeList(): List<DemoType> = enumValues<DemoType>().toMutableList()
    }
}