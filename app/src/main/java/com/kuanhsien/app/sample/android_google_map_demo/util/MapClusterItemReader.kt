package com.kuanhsien.app.sample.android_google_map_demo.util

import com.google.android.gms.maps.model.LatLng
import com.kuanhsien.app.sample.android_google_map_demo.data.MapClusterItem
import org.json.JSONArray
import org.json.JSONException
import java.io.InputStream
import java.util.*


class MapClusterItemReader {

    @Throws(JSONException::class)
    fun read(inputStream: InputStream): List<MapClusterItem> {

        val items = ArrayList<MapClusterItem>()
        val json = Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next()
        val array = JSONArray(json)

        for (i in 0 until array.length()) {

            val jsonObject = array.getJSONObject(i)
            val lat = jsonObject.getDouble("lat")
            val lng = jsonObject.getDouble("lng")

            var title: String = emptyString()
            var snippet: String = emptyString()

            if (!jsonObject.isNull("title")) {
                title = jsonObject.getString("title")
            }

            if (!jsonObject.isNull("snippet")) {
                snippet = jsonObject.getString("snippet")
            }

            items.add(MapClusterItem(LatLng(lat, lng), title, snippet))
        }

        return items
    }

    companion object {
        /**
         * This matches only once in whole input,
         * so Scanner.next returns whole InputStream as a String.
         * http://stackoverflow.com/a/5445161/2183804
         */
        private const val REGEX_INPUT_BOUNDARY_BEGINNING = "\\A"
    }

}
