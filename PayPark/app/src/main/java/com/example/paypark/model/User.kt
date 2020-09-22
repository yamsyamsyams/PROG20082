package com.example.paypark.model

import java.util.*

class User (
    var name: String,
    var email: String,
    var phoneNumber: String,
    var carPlate: String,
    var gender: String,
    var creditCardNumber: String,
    var nameOnCard: String,
    var cvv: Int,
    var password: String,
    var expiryDate: Date
){
    constructor() : this("unknown",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        "",
        Date()
    )

    fun displayUser(){
        // TODO for displaying user info
    }
}