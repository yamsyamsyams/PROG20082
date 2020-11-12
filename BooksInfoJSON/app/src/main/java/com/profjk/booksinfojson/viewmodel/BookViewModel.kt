package com.profjk.booksinfojson.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.profjk.booksinfojson.network.Book
import com.profjk.booksinfojson.network.BooksApi
import kotlinx.coroutines.launch

/*
User: hoangjam
Name: Jameson Hoang
Student ID: 991548515
Date: 2020-11-12
*/
class BookViewModel : ViewModel() {

    private val book = MutableLiveData<Book>()
    val response : LiveData<Book>
    get() = book

    fun getBookInfo(apiUrl : String){
        viewModelScope.launch{
            try{
                val book = BooksApi.RETROFIT_SERVICE_BOOKS.retrieveResponse(apiUrl)
                this@BookViewModel.book.postValue(book)
            }catch(ex: Exception){
                Log.e("BooksViewModel", ex.localizedMessage)
            }
        }
    }
}