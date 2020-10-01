package com.example.paypark.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.paypark.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAll(vararg users: User)

    @Update
    fun updateUsers(vararg users: User)

    @Delete
    fun deleteUser(user: User)

    // customized query
    @Query("DELETE FROM Users WHERE email LIKE :email")
    fun deleteUserByEmail(email: String)

    @Query("SELECT * FROM Users")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * from Users WHERE email LIKE :email")
    fun getUserByEmail(email:String) : User?

    @Query("SELECT * FROM Users WHERE email LIKE :email AND password LIKE :pw")
    fun getUserByLoginInfo(email: String, pw: String) : User?

}