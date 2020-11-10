package com.example.paypark.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.paypark.database.ParkingRepository
import com.example.paypark.managers.SharedPreferencesManager
import com.example.paypark.model.Parking
//import com.google.firestore.v1.DocumentChange
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query

class ParkingViewModel : ViewModel() {
    private val TAG = this@ParkingViewModel.toString()
    private val parkingRepository = ParkingRepository()

    fun addParking(parking: Parking) {
    parkingRepository.addParking(parking)

        Log.e("Parking View Model", "addParking : " + parking.toString())
    }

    fun getAllParkings() {
        val userEmail = SharedPreferencesManager.read(SharedPreferencesManager.EMAIL, "").toString()
        parkingRepository.getAllParking()
                .whereEqualTo("email", userEmail)
                .orderBy("parkingDate", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e(TAG, "Listening failed. No connection available.")
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        for (documentChange in snapshot.documentChanges) {
                            val parking = documentChange.document.toObject(Parking::class.java)
                            Log.e(TAG, "Parking document change : " + parking.toString())

                            when (documentChange.type) {
                                DocumentChange.Type.ADDED -> {
                                    Log.e(TAG, "Document added to collection" + parking.toString())
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    Log.e(TAG, "Document modified" + parking.toString())
                                }
                                DocumentChange.Type.REMOVED -> {
                                    Log.e(TAG, "Document deleted" + parking.toString())
                                }
                            }
                        }
                    } else {
                        Log.e(TAG, "Current Data is null")
                    }
                }
    }

    fun deleteParking(parkingId: String) {
        parkingRepository.deleteParking(parkingId)
    }
}