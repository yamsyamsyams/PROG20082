package com.example.contactsprovider.ui.home

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsprovider.model.Contact
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {

    val TAG : String = "HomeViewModel"
    val contactsList : MutableLiveData<List<Contact>> = MutableLiveData()

    fun startFetchingContacts(context: Context){
        Log.e(TAG, "Fetching contacts")

        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val PROJECTION = arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
            )

            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            )

            if(cursor != null && cursor.count > 0){
                // extract various info
                val tempContactList : MutableList<Contact> = mutableListOf()

                while(cursor.moveToNext()){
                    val tempContact = Contact()

                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                    if(name != null){
                        tempContact.name = name
                    }
                    else{
                        tempContact.name = "Unknown"
                    }

                    try{
                        if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){
                            val phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf(id),
                                null
                            )
                            if(phoneCursor != null){
                                while(phoneCursor.moveToNext()){
                                    val phoneNumber : String = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                                    tempContact.phone = phoneNumber
                                    Log.e(TAG, "phone number ${tempContact.phone}")
                                }
                            }
                            phoneCursor!!.close()
                        }
                        else{
                            Log.e(TAG, "No phone numbers found")
                        }

                        val emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        if(emailCursor != null && emailCursor.count > 0){
                            while(emailCursor.moveToNext()){
                                val email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))

                                if(email != null){
                                    tempContact.email = email
                                    Log.e(TAG, "Email : ${tempContact.email}")
                                }else{
                                    Log.e(TAG, "No email found")
                                }
                            }
                        }
                        emailCursor!!.close()

                    }catch(ex: Exception){
                        Log.e(TAG, ex.localizedMessage)
                    }

                    Log.e(TAG, "Name : ${tempContact.name}")
                    tempContactList.add(tempContact)
                }
                contactsList.value = tempContactList
            }

            cursor!!.close()
        }
    }

}