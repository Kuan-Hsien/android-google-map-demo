package com.kuanhsien.app.sample.android_google_map_demo.ui.demo

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.kuanhsien.app.sample.android_google_map_demo.BuildConfig.APPLICATION_ID
import com.kuanhsien.app.sample.android_google_map_demo.R
import com.kuanhsien.app.sample.android_google_map_demo.common.MapConstants.TAG_DEMO
import com.kuanhsien.app.sample.android_google_map_demo.util.PermissionUtil
import kotlinx.android.synthetic.main.activity_maps_location_custom_button.*


class LocationCustomButtonDemoActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private val tag = this.javaClass.simpleName
    private var googleMap: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    // Is first time to show permission dialog
    private var isFirstTimeRequestPermission: Boolean = true

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

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps_location_custom_button)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        (supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment).getMapAsync(this)

        // Main entry point for interacting with the fused location provider.
        // or use fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        // Update data in bundle
        updateValuesFromBundle(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        // if already has permission, call moveCameraOnMap()
        // this would also be called when (1) user allow this permission in settings page, and back to this app
        // (2) user exit original default permission dialog
        showLocationLayer(PermissionUtil.hasPermission(this, ACCESS_FINE_LOCATION))

        if (PermissionUtil.hasPermission(this, ACCESS_FINE_LOCATION)) {
            moveCameraOnMap()
        }
    }

    /**
     *  1.5.1. Retrieve the map's device location and camera position when the activity is paused.
     */
    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return

        // If there are some information keep in bundle, update them
        if (savedInstanceState.keySet().contains(KEY_CAMERA_POSITION)) {
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }

        if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
        }
    }

    /**
     *  1.5.2 Save the map's device location and camera position when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_CAMERA_POSITION, cameraPosition)
        outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        super.onSaveInstanceState(outState)
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

        // 1. Disable default myLocationButton
        map.uiSettings.isMyLocationButtonEnabled = false

        // 2. Set up location layer and UI based on permissions
        showLocationLayer(PermissionUtil.hasPermission(this, ACCESS_FINE_LOCATION))

        // 3. Set setOnCameraIdleListener and onClickListener for customized location button
        //      these buttons only visible after onMapReady
        map.setOnCameraIdleListener(this)
        btn_my_location_disable.setOnClickListener {
            requestPermissions()
        }
        btn_my_location_enable.setOnClickListener {
            refreshDeviceLocation()
        }

        // 4. If already has permission, call refreshDeviceLocation()
        if (PermissionUtil.hasPermission(this, ACCESS_FINE_LOCATION)) {
            moveCameraOnMap()
        } else {
            requestPermissions()
        }
    }

    /**
     *  Record current camera position once camera idle
     */
    override fun onCameraIdle() {
        cameraPosition = googleMap?.cameraPosition   // Record current camera position
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
    private fun requestPermissions() {
        when {
            // First time to request permission, user have not click OK before
            isFirstTimeRequestPermission -> {
                Log.d(TAG_DEMO, "[$tag] First time to request permission")
                showRequestPermissionDialog()
            }

            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            PermissionUtil.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION) -> {
                Log.d(TAG_DEMO, "[$tag] Displaying permission rationale to provide additional context.")
                showRequestPermissionDialog()
            }

            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            else -> {
                Log.d(TAG_DEMO, "[$tag] Requesting permission")
                PermissionUtil.requestPermissions(this, listOf(ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_ACCESS_FINE_LOCATION)
            }
        }
    }

    /**
     *  2.3. Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSIONS_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG_DEMO, "[$tag] User grant permissions.")
                    showLocationLayer(PermissionUtil.hasPermission(this, ACCESS_FINE_LOCATION))
                    refreshDeviceLocation()

                } else if (grantResults.isEmpty() ) {
                    // user cancel request, the result arrays are empty
                    Log.d(TAG_DEMO, "[$tag] User interaction was cancelled.")

                } else {
                    // Additionally, it is important to remember that a permission might have been
                    // rejected without asking the user for permission (device policy or "Never ask
                    // again" prompts). Therefore, a user interface affordance is typically implemented
                    // when permissions are denied. Otherwise, your app could appear unresponsive to
                    // touches or interactions which have required permissions.
                    showRequestPermissionInSettingDialog()
                }
            }
        }
    }

    /**
     *  3. Use showLocationLayer() method to set the location controls on the map.
     *
     *       If user has granted location permission, enable the My Location layer and the related control on the map,
     *       otherwise disable the layer and the control, and set the current location to null.
     *       Even if user revoke location permissions, we still need to keep current camera position.
     *
     *       This fun would be called in onMapReady / onResume / onRequestPermissionsResult
     */
    private fun showLocationLayer(isPermissionGranted: Boolean) {
        if (!isPermissionGranted) {
            lastKnownLocation = null
        }

        try {
            googleMap?.isMyLocationEnabled = isPermissionGranted
            btn_my_location_enable.isVisible = isPermissionGranted
            btn_my_location_disable.isVisible = !isPermissionGranted
        } catch (e: SecurityException) {
            Log.e(TAG_DEMO, "[$tag] Exception: ${e.message}")
        }
    }

    /**
     *  4. Move camera to cameraPosition.
     *       This fun would be called in onMapReady (false) / onResume (false) / refreshDeviceLocation (true)
     *
     *  @param hasRefreshLocation False if we need to refresh device location when cameraPosition is null
     */
    private fun moveCameraOnMap(hasRefreshLocation: Boolean = false) {
        Log.d(TAG_DEMO, "[$tag] Restore the map's camera position")

        googleMap?.let { map ->
            if (cameraPosition != null) {
                // 1. If user had move the camera previously, just restore the map's camera position.
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            } else {
                // 2. When cameraPosition is null and haven't refresh device location yet, call refreshDeviceLocation()
                //      only called refreshDeviceLocation once
                if (!hasRefreshLocation) {
                    refreshDeviceLocation()
                } else {
                    Log.d(TAG_DEMO, "[$tag] couldn't get device location to move camera")
                }
            }
        } ?: Log.e(TAG_DEMO, "[$tag] GoogleMap is null")
    }

    /**
     *  5. Get the location of the Android device and position the map's camera. This fun would be called when
     *      (1) click MyLocationButton
     *      (2) onRequestPermissionsResult (when successfully get location permission)
     *      (3) while cameraPosition is null and have location permission
     *          => onMapReady / onResume would call moveCameraOnMap(false)
     */
    private fun refreshDeviceLocation() {
        Log.d(TAG_DEMO, "[$tag] Call refreshDeviceLocation()")

        try {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->

                // 1. Retrieve lastKnownLocation from task result
                if (task.isSuccessful) {
                    if (task.result != null) {
                        lastKnownLocation = task.result
                    } else {
                        Log.d(TAG_DEMO, "[$tag] getLastLocation is null. Use lastKnownLocation.")
                        Toast.makeText(this, "getLastLocation is null. Use lastKnownLocation.", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Log.e(TAG_DEMO, "[$tag] Exception: ${task.exception}")
                    Toast.makeText(this, "getLastLocation Fail", Toast.LENGTH_LONG).show()
                }

                // 2. Set camera position to lastKnownLocation
                lastKnownLocation?.let { lastKnownLocation ->
                    cameraPosition = CameraPosition.fromLatLngZoom(
                        LatLng(
                            lastKnownLocation.latitude,
                            lastKnownLocation.longitude
                        ), DEFAULT_ZOOM.toFloat())
                }

                // 3. Move camera
                moveCameraOnMap(true)
            }
        } catch (e: SecurityException) {
            Log.e(TAG_DEMO, "[$tag] Exception: ${e.message}")
        }
    }

    /**
     *  Show dialog to explain the permission rationale
     */
    private fun showRequestPermissionDialog() =
        AlertDialog.Builder(this)
            .setMessage("Turn on location service to show current location")
            .setPositiveButton("OK") { _, _ ->

                isFirstTimeRequestPermission = false
                PermissionUtil.requestPermissions(this, listOf(ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_ACCESS_FINE_LOCATION)
            }
            .setNegativeButton("NO") { _, _ -> } // just dismiss dialog
            .show()

    /**
     *  Show dialog to explain the permission rationale with action to open settings
     */
    private fun showRequestPermissionInSettingDialog() =
        AlertDialog.Builder(this)
            .setMessage("Location service is disabled. Grant permission to access current location in settings")
            .setPositiveButton("OK") { _, _ ->

                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
            .setNegativeButton("NO") { _, _ -> } // just dismiss dialog
            .show()

    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val REQUEST_PERMISSIONS_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "key_camera_position"
        private const val KEY_LOCATION = "key_location"
    }
}