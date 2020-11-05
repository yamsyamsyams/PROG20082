package com.example.paypark.database

import com.example.paypark.model.Parking
import com.google.firebase.ktx.Firebase

class ParkingRepository {
    private val COLLECTION_NAME = "ParkingList"
    private val db = Firebase.firestore
    private val TAG = this.toString()

    fun addParking(parking : Parking){
        db.collection(COLLECTION_NAME).document(parking.id.toString()).set(parking)

    }

    fun getAllParking(){

    }

    fun updateParking(){

    }

    fun deleteParking(){

    }
}