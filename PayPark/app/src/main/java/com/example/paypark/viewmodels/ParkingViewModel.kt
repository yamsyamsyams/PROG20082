package com.example.paypark.viewmodels

import androidx.lifecycle.ViewModel
import com.example.paypark.database.ParkingRepository
import com.example.paypark.model.Parking

class ParkingViewModel : ViewModel() {
    private val parkingRepository = ParkingRepository()

    fun addParking(parking: Parking){
        parkingRepository.addParking(parking)
    }
}