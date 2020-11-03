package com.example.paypark.managers

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private var sharedPreferences: SharedPreferences? = null

    val EMAIL = "KEY_EMAIL"
    val PASSWORD = "KEY_PASSWORD"

    fun init (context: Context) {
        if (sharedPreferences == null){
            sharedPreferences = context.getSharedPreferences(context.packageName,
                Activity.MODE_PRIVATE)
        }
    }

    fun write(key: String?, value: String?){
//        sharedPreferences?.edit()!!.putString(key, value).commit()

//        with(sharedPreferences!!.edit()){
//            putString(key, value)
//            commit()
//        }

        apply { sharedPreferences!!.edit().putString(key, value).apply() }
    }

    fun read(key: String?, defaultValue: String?): String?{

        with(sharedPreferences) {
//            return sharedPreferences!!.getString(key, defaultValue)

            if (this!!.contains(key)){
                return sharedPreferences!!.getString(key,defaultValue)
            }
        }

        return defaultValue
    }

    fun remove(key: String?){

        if ( sharedPreferences != null && sharedPreferences!!.contains(key)) {
            apply { sharedPreferences?.edit()!!.remove(key).apply() }
        }
    }

    fun removeAll(){
        //takehome - modify the method to check if shared pref in not null and contains the keys

        with(sharedPreferences!!.edit()){
//            remove("KEY_EMAIL")
//            remove("KEY_PASSWORD")

            remove(EMAIL)
            remove(PASSWORD)

            apply()  //asynchronously
//            commit() //synchronously
        }
    }

}