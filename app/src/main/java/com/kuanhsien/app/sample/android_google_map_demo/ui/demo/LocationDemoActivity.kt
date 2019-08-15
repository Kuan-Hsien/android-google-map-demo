package com.kuanhsien.app.sample.android_google_map_demo.ui.demo

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.kuanhsien.app.sample.android_google_map_demo.R
import com.kuanhsien.app.sample.android_google_map_demo.common.MapConstants.TAG_DEMO


class LocationDemoActivity : AppCompatActivity(), OnMapReadyCallback {

    private val tag = this.javaClass.simpleName
    private var googleMap: GoogleMap? = null
    private var mCameraPosition: CameraPosition? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is not granted.
    private val mDefaultLocation = LatLng(-33.8523341, 151.2106085)

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var mLastKnownLocation: Location? = null

    /**
     *  1. Add a map
     *
     *     1.1. prepare <fragment> in xml
     *     1.2. In activity's onCreate() method, set the layout file as the content view
     *     1.3. In activity's onCreate() method, get a handle to the map fragment by
     *          calling FragmentManager.findFragmentById(). Then use getMapAsync() to register for the map callback
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        (supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment).getMapAsync(this)

        // Main entry point for interacting with the fused location provider.
        // or use fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient = FusedLocationProviderClient(this)
    }

    /**
     *  1.4. Implement the OnMapReadyCallback interface and override the onMapReady() method,
     *       to set up the map when the GoogleMap object is available
     *       This callback is triggered when the map is ready to be used.
     *       This is where we can add markers or lines, add listeners or move the camera. In this case,
     *
     *       If Google Play services is not installed on the device, the user will be prompted to install
     *       it inside the SupportMapFragment. This method will only be triggered once the user has
     *       installed Google Play services and returned to the app.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Prompt the user for permission.
        getLocationPermission()
    }


    /**
     *  2. Request location permission
     *     Your app must request location permission in order to determine the location of the device and to
     *     allow the user to tap the My Location button on the map.
     *
     *     2.1. Add the permission as a child of the <manifest> element in your Android manifest:
     *     2.2. Request runtime permissions.
     *          Checks whether the user has granted fine location permission. If not, it requests the permission.
     */
    private fun getLocationPermission() {
        // If already get permission
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Turn on the My Location layer and the related control on the map.
            setLocationLayerUI(true)

            // Get the current location of the device and set the position of the map.
            showDeviceLocation()

        } else {

            setLocationLayerUI(false)

            /*
             * Request location permission, so that we can get the location of the
             * device. The result of the permission request is handled by a callback,
             * onRequestPermissionsResult.
             */
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    /**
     *  2.3. Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Turn on the My Location layer and the related control on the map.
                    setLocationLayerUI(true)

                    // Get the current location of the device and set the position of the map.
                    showDeviceLocation()

                } else {

                    setLocationLayerUI(false)
                    Toast.makeText(this, "Please grant permission to show your location", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     *  3. Use setLocationLayerUI() method to set the location controls on the map.
     *       If the user has granted location permission, enable the My Location layer
     *       and the related control on the map,
     *       otherwise disable the layer and the control, and set the current location to null:
     *
     *  Updates the map's UI settings based on whether the user has granted location permission.
     */
    private fun setLocationLayerUI(hasPermission: Boolean) {

        if (!hasPermission) {
            mLastKnownLocation = null
        }

        googleMap?.let { map ->
            try {
                map.isMyLocationEnabled = hasPermission
                map.uiSettings.isMyLocationButtonEnabled = hasPermission

            } catch (e: SecurityException) {
                Log.e(TAG_DEMO, "[$tag] Exception: ${e.message}")
            }
        }
    }

    /**
     *  4. Get the location of the Android device and position the map
     *
     *     Use the fused location provider to find the device's last-known location, then use that location
     *     to position the map. The tutorial provides the code you need.
     *     For more details on getting the device's location,
     *     see the guide to the fused location provider in the Google Play services location APIs.
     *
     *  Gets the current location of the device, and positions the map's camera.
     */
    private fun showDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            val locationResult = fusedLocationProviderClient.lastLocation

            locationResult.addOnCompleteListener(this) { task ->

                googleMap?.let { map ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.result
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(mLastKnownLocation!!.latitude,
                                    mLastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                    } else {
                        Log.d(TAG_DEMO, "[$tag] Current location is null. Using defaults.")
                        Log.e(TAG_DEMO, "[$tag] Exception: %s", task.exception)
                        map.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM.toFloat()))
                        map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG_DEMO, "[$tag] Exception: ${e.message}")
        }
    }
    

    /**
     *  5. Save the map's camera position and the device location. When a user rotates an Android device,
     *     or makes configuration changes, the Android framework destroys and rebuilds the map activity.
     *     To ensure a smooth user experience, it's good to store relevant application state and restore it when needed
     *
     *  Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        googleMap?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation)
            super.onSaveInstanceState(outState)
        }
    }

    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}
