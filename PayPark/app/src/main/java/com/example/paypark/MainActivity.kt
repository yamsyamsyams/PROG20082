package com.example.paypark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // receive data from previous activity through Intent

        // obtain the instance of current Intent
        val currentIntent = this.intent
        val email = currentIntent.extras?.getString("com.example.paypark.EXTRA_MAIL")

        // make sure that the textView is assigned an id on layout
        tvEmail.text = email
    }
}