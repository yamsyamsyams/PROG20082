package com.example.paypark.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.paypark.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // receive data from previous activity through Intent

        // obtain the instance of current Intent
//        val currentIntent = this.intent

//        val email = currentIntent.extras?.getString("com.example.paypark.EXTRA_MAIL")

//        val user = currentIntent.extras?.get("com.example.paypark.EXTRA_USER") as User
//        tvEmail.text = user.email.toString()
//        tvPhone.text = user.phoneNumber.toString()
//        tvExpiryDate.text = user.expiryDate.toString()

        // make sure that the textView is assigned an id on layout
//        tvEmail.text = email
    }
}