package com.example.locationservices

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity() {
    private val TAG = this@MainActivity.toString()
    private lateinit var locationManager: LocationManager
    private lateinit var location: Location
    private lateinit var currentLocation: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.locationManager = LocationManager(this@MainActivity)

        if(LocationManager.locationPermissionsGranted){
            this.getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LocationManager.LOCATION_PERMISSION_REQUEST_CODE -> {
                LocationManager.locationPermissionsGranted = (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                if(LocationManager.locationPermissionsGranted){
                    // location available
                    // try to fetch the location

                    this.getLastLocation()
                }
                return
            }
        }
    }

    private fun getLastLocation(){
        this.locationManager.getLastLocation()?.observe(this, {loc: Location? ->
            if(loc != null){
                this.location = loc
                this.currentLocation = LatLng(location.latitude, location.longitude)
                Log.e(TAG, "current location : " + this.currentLocation.toString())
            }
        })
    }
}