package com.example.paypark.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Users", primaryKeys = arrayOf("email"))
data class User (
    @ColumnInfo(name = "name") var name: String,
//    @PrimaryKey @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "phone_number") var phoneNumber: String?,
    @ColumnInfo(name = "car_plate") var carPlate: String,
    @ColumnInfo(name = "gender") var gender: String?,
    @ColumnInfo(name = "card_number") var creditCardNumber: String,
    @ColumnInfo(name = "name_on_card") var nameOnCard: String?,
    @ColumnInfo(name = "cvv") var cvv: Int?,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "expiry_date") var expiryDate: Date?
){
    constructor() : this(
        "",
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
}