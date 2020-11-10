package com.example.locationservices

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationManager(var context: Context) {
    private val TAG = this.toString()
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var location: MutableLiveData<Location> = MutableLiveData()
    lateinit var locationRequest: LocationRequest

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 102
        var locationPermissionsGranted = false
    }

    init {
        checkPermissions()
        getLocationProviderClient()
    }

    fun getLocationProviderClient(): FusedLocationProviderClient {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.context)
        }
        return fusedLocationProviderClient!!
    }

    fun getLastLocation() : LiveData<Location>?{
        if (locationPermissionsGranted){
            try{
                this.fusedLocationProviderClient!!.lastLocation.addOnSuccessListener { loc: Location? ->
                    // last location is available
                    if(loc != null){
                        location.value = loc
                        Log.e(TAG, "Last location : " + location.value.toString())
                    }
                }.addOnFailureListener{ error ->
                    Log.e(TAG, "error : " + error.localizedMessage)
                }
            }
            catch(ex: SecurityException){
                Log.e(TAG, "Security Exception : " + ex.localizedMessage)
            }
            return location
        }
        return null
    }

    private fun checkPermissions() {
        locationPermissionsGranted = (ContextCompat.checkSelfPermission(
            this.context.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)

        Log.e(TAG, "locationPermissionsGranted " + locationPermissionsGranted.toString())

        if (!locationPermissionsGranted) {
            this.requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this.context as Activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}