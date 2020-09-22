package com.example.paypark.utils

import android.text.TextUtils
import android.util.Patterns

class DataValidations {
    fun validateEmail(email : String) : Boolean{
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}