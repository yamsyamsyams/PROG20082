package com.example.contactsprovider.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsprovider.R
import com.example.contactsprovider.model.Contact
import kotlinx.android.synthetic.main.contact_list_item.view.*

/*
User: hoangjam
Name: Jameson Hoang
Student ID: 991548515
Date: 2020-12-01
*/
class ContactsAdapter(
    val context: Context,
    val contactList: ArrayList<Contact>
) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactsAdapter.ContactsViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.contact_list_item, null)
        return ContactsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactsAdapter.ContactsViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvName : TextView = itemView.tvName
        var tvEmail : TextView = itemView.tvEmail
        var tvPhone : TextView = itemView.tvPhone

        fun bind(contact: Contact){
            tvName.setText(contact.name)
            tvEmail.setText(contact.email)
            tvPhone.setText(contact.phone)
        }
    }
}