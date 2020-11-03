package com.example.paypark.viewmodels

//https://github.com/ProfJK/PayPark-Android-F20.git

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.paypark.database.PayParkDatabase
import com.example.paypark.database.UserRepository
import com.example.paypark.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepo: UserRepository
    var allUsers: LiveData<List<User>>

    private var matchedUser : MutableLiveData<User>?

    init {
        val userDao = PayParkDatabase.getDatabase(application).userDao()
        userRepo = UserRepository(userDao)

        allUsers = userRepo.allUsers

        matchedUser = MutableLiveData()
    }

    /**
     * insertAll() method will create a new user record in the database
     */
    fun insertAll(user: User) = viewModelScope.launch(Dispatchers.IO){
        userRepo.insertAll(user)
    }

    fun updateUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepo.updateUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepo.deleteUser(user)
    }

    fun deleteUserByEmail(email: String) = viewModelScope.launch (Dispatchers.IO){
        userRepo.deleteUserByEmail(email)
    }

    private fun getUserByLoginInfoCoroutine(email: String, password: String)  = viewModelScope.launch(Dispatchers.IO) {
        val user : User? = userRepo.getUserByLoginInfo(email, password)
        matchedUser?.postValue(user)
    }

    fun getUserByLoginInfo(email: String, password: String) : MutableLiveData<User>?{
        getUserByLoginInfoCoroutine(email, password)

        return matchedUser
    }

    private  fun getUserByEmailCoroutine(email: String) = viewModelScope.launch (Dispatchers.IO){
        val user: User? = userRepo.getUserByEmail(email)
        matchedUser?.postValue(user)
    }

    fun getUserByEmail(email: String) : MutableLiveData<User>?{
        getUserByEmailCoroutine(email)

        Log.d("UserViewModel : ", matchedUser.toString())

        return matchedUser
    }
}


















