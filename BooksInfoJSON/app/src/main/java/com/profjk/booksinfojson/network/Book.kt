package com.profjk.booksinfojson.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
User: hoangjam
Name: Jameson Hoang
Student ID: 991548515
Date: 2020-11-12
*/

@JsonClass(generateAdapter = true)
data class Book(
    @Json(name="items") val bookItem: Array<Item>? = null
)

data class Item(
    val volumeInfo: VolumeInfo? = null,
    @Json(name = "saleInfo") val saleInfo: SaleInfo? = null // even safer, we can include Json name even if it's the same
)

data class VolumeInfo(
    val authors : Array<String?> = arrayOf(null),
    val publisher : String? = null,
    @Json(name = "categories") val genre : Array<String?> = arrayOf(null),
    var averageRating : Double? = null
)

data class SaleInfo(
    val retailPrice: RetailPrice?
)

data class RetailPrice(
    @Json(name = "amount") val price : Double? = null,
    val currencyCode : String? = null
)