package com.example.paypark.model

import java.util.*

data class Parking(
        var id: String = UUID.randomUUID().toString(),
        var email: String = "",
        var carPlate: String = "",
        var buildingCode: Long? = null,
        var unitNumber: String? = null,
        var duration: Long? = null,
        var parkingDate: Date = Date(),
        var parkingLocation: String = ""
)