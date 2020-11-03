package com.example.paypark.utils

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.util.Patterns
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class DataValidations {
    fun validateEmail(email: String) : Boolean{
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun encryptPassword(password: String) : String{
        try{
            val md = MessageDigest.getInstance("SHA-256")
            md.update(password.toByteArray())

            val hashPass = md.digest()

            return Base64.encodeToString(hashPass, Base64.DEFAULT)

        }catch(ex: NoSuchAlgorithmException){
            Log.e("Data Validations", ex.localizedMessage)
        }

        return ""
    }
}