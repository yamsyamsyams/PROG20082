package com.example.paypark.database

import android.util.Log
import com.example.paypark.model.Parking
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ParkingRepository {
    private val COLLECTION_NAME = "ParkingList"
    private val db = Firebase.firestore
    private val TAG = this.toString()

    fun addParking(parking : Parking){
        db.collection(COLLECTION_NAME)
                .document(parking.id.toString())
                .set(parking)
                .addOnSuccessListener { Log.e(TAG, "Document successfully added") }
                .addOnFailureListener{error -> Log.e(TAG, "Unable to add a document" + error.localizedMessage)}

        Log.e(TAG, "addParking : " + parking.toString())
    }

    fun getAllParking() : CollectionReference{
        val collectionReference = db.collection(COLLECTION_NAME)
        Log.e(TAG, "Collection Reference : " + collectionReference.id)
        return collectionReference
    }

    fun updateParking(){

    }

    fun deleteParking(parkingId : String){
        db.collection(COLLECTION_NAME)
                .document(parkingId)
                .delete()
                .addOnSuccessListener { Log.e(TAG, "Document successfully deleted") }
                .addOnFailureListener{error -> Log.e(TAG, "Unable to delete a document" + error.localizedMessage)}
    }
}