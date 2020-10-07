package com.example.paypark.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paypark.R
import com.example.paypark.model.User
import com.example.paypark.utils.DateConverter

@Database(entities = arrayOf(User::class), version = 1)
@TypeConverters(*arrayOf(DateConverter::class))
abstract class PayParkDatabase : RoomDatabase(){

    abstract fun userDao(): UserDao
    companion object{

        @Volatile
        private var INSTANCE: PayParkDatabase? = null

        fun getDatabase(context: Context) : PayParkDatabase{
            val tempInstance = INSTANCE

            //if there is already some instance of the database, return that
            if (tempInstance != null){
                return tempInstance
            }

            //otherwise, create new database
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PayParkDatabase::class.java,
                    R.string.database_name.toString()
                ).build()

                INSTANCE = instance
                return instance
            }

        }
    }
}