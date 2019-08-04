package com.kuanhsien.app.sample.android_google_map_demo.ui.map

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.kuanhsien.app.sample.android_google_map_demo.R


enum class PolygonType(val tag: String) {
    PolygonGreen("polygon_green"),
    PolygonOrange("polygon_orange")
    ;
}


enum class PolylineType(val tag: String) {
    PolylineCustom("polyline_custom"),
    PolylineDefault("polyline_default")
    ;
}


/**
 * An activity that displays a Google map with polylines to represent paths or routes,
 * and polygons to represent areas.
 */
class MapPolygonActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this tutorial, we add polylines and polygons to represent routes and areas on the map.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        /**
         * [Polyline Sample]
         *
         * A Polyline is a series of connected line segments. Polylines are useful to
         * represent routes, paths, or other connections between locations on the map.
         *
         * 1. Create a PolylineOptions object and add points to it. Each point represents a location on the map,
         *    which you define with a LatLng object containing latitude and longitude values.
         *    The code sample below creates a polyline with 6 points.
         *
         * 2. Call GoogleMap.addPolyline() to add the polyline to the map.
         *
         * 3. Set the polyline's clickable option to true if you want to handle click events on the polyline.
         */
        // Add polylines to the map.
        // Polylines are useful to show a route or some other connection between points.
        val polyline1 = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(-35.016, 143.321),
                    LatLng(-34.747, 145.592),
                    LatLng(-34.364, 147.891),
                    LatLng(-33.501, 150.217),
                    LatLng(-32.306, 149.248),
                    LatLng(-32.491, 147.309)
                ))

        /**
         * [Polyline Style]
         * You can store arbitrary data objects with polylines and other geometry objects.
         *
         * 1. Call Polyline.setTag() to store a data object with the polyline. The code below
         *    defines an arbitrary tag (A) indicating a type of polyline.
         *
         * 2. Retrieve the data using Polyline.getTag(), as the next section shows.
         */
        // Store a data object with the polyline, used here to indicate an arbitrary type.
        polyline1.tag = PolylineType.PolylineCustom.tag
        // Style the polyline.
        stylePolyline(polyline1)

        val polyline2 = googleMap.addPolyline(
            PolylineOptions()
                .clickable(true)
                .add(
                    LatLng(-29.501, 119.700),
                    LatLng(-27.456, 119.672),
                    LatLng(-25.971, 124.187),
                    LatLng(-28.081, 126.555),
                    LatLng(-28.848, 124.229),
                    LatLng(-28.215, 123.938)
                ))
        polyline2.tag = PolylineType.PolylineDefault.tag
        stylePolyline(polyline2)


        /**
         * [Polygon Sample]
         *
         * A Polygon is a shape consisting of a series of coordinates in an ordered sequence, similar to a Polyline.
         * The difference is that polygon defines a closed area with a fillable interior,
         * while a polyline is open ended.
         *
         * 1. Create a PolygonOptions object and add points to it. Each point represents a location on the map,
         *    which you define with a LatLng object containing latitude and longitude values.
         *    The code sample below creates a polygon with 4 points.
         *
         * 2. Make the polygon clickable by calling Polygon.setClickable().
         *    (By default, polygons are not clickable and your app will not receive a notification when
         *    the user taps a polygon.) Handling polygon click events is similar to handling the events on polylines.
         *
         * 3. Call GoogleMap.addPolygon() to add the polygon to the map.
         *
         * 4. Call Polygon.setTag() to store a data object with the polygon. The code below
         *    defines an arbitrary type for the polygon.
         */
        // Add polygons to indicate areas on the map.
        val polygon1 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(-27.457, 153.040),
                    LatLng(-33.852, 151.211),
                    LatLng(-37.813, 144.962),
                    LatLng(-34.928, 138.599)
                ))
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.tag = PolygonType.PolygonGreen.tag
        // Style the polygon.
        stylePolygon(polygon1)

        val polygon2 = googleMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(-31.673, 128.892),
                    LatLng(-31.952, 115.857),
                    LatLng(-17.785, 122.258),
                    LatLng(-12.4258, 130.7932)
                ))
        polygon2.tag = PolygonType.PolygonOrange.tag
        stylePolygon(polygon2)

        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.684, 133.903), 4f))

        // Set listeners for click events.
        googleMap.setOnPolylineClickListener(this)
        googleMap.setOnPolygonClickListener(this)
    }

    /**
     * Styles the polyline, based on type.
     * @param polyline The polyline object that needs styling.
     *
     * You can specify various styling properties in the PolylineOptions object.
     * Styling options include the stroke color, stroke width, stroke pattern, joint types, and start and end caps.
     * If you don't specify a particular property, the API uses a default for that property.
     */
    private fun stylePolyline(polyline: Polyline) {

        // Get the data object stored with the polyline.
        polyline.tag?.let { typeTag ->
            when (typeTag) {
                // If no type is given, allow the API to use the default.
                PolylineType.PolylineCustom.tag -> {
                    // Use a custom bitmap as the cap at the start of the line.
                    polyline.startCap = CustomCap(
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow), 10f
                    )
                }

                PolylineType.PolylineDefault.tag -> {
                    // Use a round cap at the start of the line.
                    polyline.startCap = RoundCap()
                }
            }

            polyline.endCap = RoundCap()
            polyline.width = POLYLINE_STROKE_WIDTH_PX.toFloat()
            polyline.color = ContextCompat.getColor(this, R.color.color_black_argb)
            polyline.jointType = JointType.ROUND
        }
    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private fun stylePolygon(polygon: Polygon) {

        var pattern = listOf<PatternItem>()
        var strokeColor = ContextCompat.getColor(this, R.color.color_black_argb)
        var fillColor = ContextCompat.getColor(this, R.color.color_white_argb)

        // Get the data object stored with the polygon.
        polygon.tag?.let { typeTag ->

            when (typeTag) {
                // If no type is given, allow the API to use the default.
                PolygonType.PolygonGreen.tag -> {
                    // Apply a stroke pattern to render a dashed line, and define colors.
                    pattern = PATTERN_POLYGON_GREEN
                    strokeColor = ContextCompat.getColor(this, R.color.color_green_stroke)
                    fillColor = ContextCompat.getColor(this, R.color.color_green_fill)
                }

                PolygonType.PolygonOrange.tag -> {
                    // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                    pattern = PATTERN_POLYGON_ORANGE
                    strokeColor = ContextCompat.getColor(this, R.color.color_orange_stroke)
                    fillColor = ContextCompat.getColor(this, R.color.color_orange_fill)
                }
            }
        }

        polygon.strokePattern = pattern
        polygon.strokeWidth = POLYGON_STROKE_WIDTH_PX.toFloat()
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
    }

    /**
     * Listens for clicks on a polyline.
     * @param polyline The polyline object that the user has clicked.
     *
     * Override the onPolylineClick() callback method. The following example
     * alternates the stroke pattern of the line between solid and dotted, each time the user clicks the polyline
     */
    override fun onPolylineClick(polyline: Polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if (polyline.pattern == null || !polyline.pattern!!.contains(DOT)) {
            polyline.pattern = PATTERN_POLYLINE_DOTTED

        } else {
            // The default pattern is a solid stroke.
            polyline.pattern = null
        }

        Toast.makeText(this, "Route type " + polyline.tag!!.toString(), Toast.LENGTH_SHORT).show()
    }

    /**
     * Listens for clicks on a polygon.
     * @param polygon The polygon object that the user has clicked.
     */
    override fun onPolygonClick(polygon: Polygon) {
        // Flip the values of the red, green, and blue components of the polygon's color.
        var color = polygon.strokeColor xor 0x00ffffff
        polygon.strokeColor = color
        color = polygon.fillColor xor 0x00ffffff
        polygon.fillColor = color

        Toast.makeText(this, "Area type " + polygon.tag!!.toString(), Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val POLYLINE_STROKE_WIDTH_PX = 12
        private const val POLYGON_STROKE_WIDTH_PX = 8
        private const val PATTERN_DASH_LENGTH_PX = 20
        private const val PATTERN_GAP_LENGTH_PX = 20

        private val DOT = Dot()
        private val DASH = Dash(PATTERN_DASH_LENGTH_PX.toFloat())
        private val GAP = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

        // Create a stroke pattern of a gap followed by a dot.
        private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

        // Create a stroke pattern of a gap followed by a dash.
        private val PATTERN_POLYGON_GREEN = listOf(GAP, DASH)

        // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
        private val PATTERN_POLYGON_ORANGE = listOf(DOT, GAP, DASH, GAP)
    }
}