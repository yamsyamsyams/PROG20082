package com.example.contactsprovider.model

/*
User: hoangjam
Name: Jameson Hoang
Student ID: 991548515
Date: 2020-12-01
*/
class Contact(
    var name: String,
    var email: String,
    var phone: String
) {
    constructor() : this(
        "",
        "",
        ""
    )
}