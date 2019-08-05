package com.kuanhsien.app.sample.android_google_map_demo.data

import androidx.appcompat.app.AppCompatActivity
import com.kuanhsien.app.sample.android_google_map_demo.R
import com.kuanhsien.app.sample.android_google_map_demo.ui.map.MarkerRetapCloseInfoDemoActivity
import com.kuanhsien.app.sample.android_google_map_demo.ui.map.MarkerSimpleDemoActivity
import com.kuanhsien.app.sample.android_google_map_demo.ui.map.PolygonSimpleDemoActivity

enum class DemoType(val titleRes: Int, val activityClass: Class<out AppCompatActivity>) {

    MarkerSimpleDemo(R.string.title_activity_marker_simple_demo, MarkerSimpleDemoActivity::class.java),
    MarkerRetapCloseInfoDemo(R.string.title_activity_marker_retap_close_info_demo, MarkerRetapCloseInfoDemoActivity::class.java),
    PolygonSimpleDemo(R.string.title_activity_polygon_simple_demo, PolygonSimpleDemoActivity::class.java)
    ;

    companion object {
        fun getDemoTypeList(): List<DemoType> = enumValues<DemoType>().toMutableList()
    }
}