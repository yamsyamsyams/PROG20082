package com.example.paypark.database

import androidx.lifecycle.LiveData
import com.example.paypark.model.User

class UserRepository(
    private val userDao: UserDao
) {
    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun insertAll(user: User){
        userDao.insertAll(user)
    }

    //initiate a background thread for executing the query
    suspend fun updateUser(user: User){
        userDao.updateUsers(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteUserByEmail(email: String){
        userDao.deleteUserByEmail(email)
    }

    suspend fun getUserByEmail(email: String) : User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun getUserByLoginInfo(email: String, password: String) : User?{
        return userDao.getUserByLoginInfo(email, password)
    }
}