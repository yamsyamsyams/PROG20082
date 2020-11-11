package com.example.locationservices


import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = this@MainActivity.toString()
    private lateinit var locationManager: LocationManager
    private lateinit var location: Location
    private lateinit var currentLocation : LatLng
    private var map: GoogleMap? = null
    private val DEFAULT_ZOOM : Float = 15.0F  //1: world, 5: landmass/continent, 10: city, 15: streets, 20: building
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.locationManager = LocationManager(this@MainActivity)
        this.currentLocation = LatLng(0.0, 0.0)

        if (LocationManager.locationPermissionsGranted){
            this.getLastLocation()
        }

        val mapFragment  = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?){
                locationResult ?: return

                for(location in locationResult.locations){
                    currentLocation = LatLng(location.latitude, location.longitude)
                    addMarkerOnMap(currentLocation)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        locationManager.requestLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        locationManager.fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LocationManager.LOCATION_PERMISSION_REQUEST_CODE -> {
                LocationManager.locationPermissionsGranted = (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)

                if (LocationManager.locationPermissionsGranted){
                    //location available
                    //try to fetch the location

                    this.getLastLocation()
                }
                return
            }
        }
    }

    private fun getLastLocation(){
        this.locationManager.getLastLocation()?.observe(this, {loc: Location? ->
            if (loc != null){
                this.location = loc
                this.currentLocation = LatLng(location.latitude, location.longitude)

                Log.e(TAG, "current location : " + this.currentLocation.toString())

                //display the location on map
                this.addMarkerOnMap(this.currentLocation)
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.getLastLocation()

        if (googleMap != null){
            googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isZoomGesturesEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            googleMap.uiSettings.isScrollGesturesEnabled = true

            googleMap.addMarker(
                MarkerOptions().position(this.currentLocation).title("You're Here")
            )

            this.map = googleMap
        }else{
            Log.e(TAG, "Map not ready yet")
        }
    }

    private fun addMarkerOnMap(location: LatLng){
        if (this.map != null){
            this.map!!.addMarker(
                MarkerOptions().position(location).title("Current Location")
            )

            this.map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
        }
    }

}