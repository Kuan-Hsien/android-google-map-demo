package com.kuanhsien.app.sample.android_google_map_demo.data

import com.kuanhsien.app.sample.android_google_map_demo.R

enum class DemoType(val titleRes: Int) {
    MapSimple(R.string.title_map_simple_activity),
    MapWithPolygon(R.string.title_map_polygon_activity)
    ;

    companion object {
        fun getDemoTypeList(): List<DemoType> = enumValues<DemoType>().toMutableList()
    }
}