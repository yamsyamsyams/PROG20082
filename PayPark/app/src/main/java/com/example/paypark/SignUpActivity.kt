package com.example.paypark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isEmpty
import com.example.paypark.model.User
import com.example.paypark.utils.DataValidations
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    val TAG: String = this@SignUpActivity.toString()

    var selectedGender: String = ""
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        this.intializeSpinner()
        selectedGender = resources.getStringArray(R.array.gender_array).get(0)

        spnGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGender = resources.getStringArray(R.array.gender_array).get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedGender = resources.getStringArray(R.array.gender_array).get(2)
            }
        }
        btnSignUp.setOnClickListener(this)
        edtExpiry.setOnClickListener(this) // display the datePicker

    }

    fun intializeSpinner() {
        val genderAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.gender_array)
        )

        spnGender.adapter = genderAdapter
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                btnSignUp.id -> {
                    // gather all the data and create object of User class
                    if(this.validateData()){
                        this.fetchData()
                    }
                }
                edtExpiry.id -> {
                }
            }
        }
    }

    fun fetchData() {
        user = User()

        user.name = edtName.text.toString()
        user.email = edtEmail.text.toString()
        user.phoneNumber = edtPhoneNumber.text.toString()
        user.carPlate = edtPlateNumber.text.toString()
        user.creditCardNumber = edtCardNumber.text.toString()
        user.nameOnCard = edtCardName.text.toString()
        user.cvv = edtCVV.text.toString().toInt()
        user.password = edtPassword.text.toString()
        user.gender = selectedGender

//        user.expiryDate
        Log.d(TAG, user.toString())

    }

    fun validateData(): Boolean {
        if (edtName.text.toString().isEmpty()) {
            edtName.error = "Please enter name"
            return false
        }

        if (edtEmail.text.toString().isEmpty()){
            edtEmail.error = "Email cannot be empty"
            return false
        }else if(!DataValidations().validateEmail(edtEmail.text.toString())){
            edtEmail.error = "Please provide valid email address"
            return false
        }

        if(edtPhoneNumber.text.toString().isEmpty()){
            edtPhoneNumber.error = "Phone number cannot be empty"
            return false
        }
        else if(edtPhoneNumber.text.toString().length > 10){
            edtPhoneNumber.error = "Please enter valid phone number length"
            return false
        }

        if(edtPlateNumber.text.toString().isEmpty()){
            edtPlateNumber.error = "Plate number cannot be blank"
            return false
        }
        else if(edtPlateNumber.text.toString().length > 7){
            edtPlateNumber.error = "Plate number must be less than 7 characters"
            return false
        }

        if(edtCardNumber.text.toString().isEmpty()){
            edtCardNumber.error = "Card number is required"
            return false
        }
        else if(edtCardNumber.text.toString().length != 16){
            edtCardNumber.error = "Card number length invalid"
            return false
        }

        if(edtCardName.text.toString().isEmpty()){
            edtCardName.error = "Name of owner of card required"
            return false
        }

        if(edtCVV.text.toString().isEmpty()){
            edtCVV.error = "CVV security code required"
            return false
        }
        else if(edtCVV.text.toString().length != 3){
            edtCVV.error = "Invalid CVV length"
            return false
        }

        if(edtPassword.text.toString().isEmpty()){
            edtPassword.error = "Password cannot be empty"
            return false
        }
        else if(edtPassword.text.toString().length < 8){
            edtPassword.error = "Password must be at least 8 characters long"
            return false
        }

        if(spnGender.toString().isEmpty()){
            tvGender.error = "Please select a gender"
            return false
        }
        //take home - add errors and data validations for the remaining inputs

        return true
    }
}