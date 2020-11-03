package com.example.paypark.utils

fun Double.round(decimals: Int) : Double{
    var multipler = 1.0
    repeat(decimals){ multipler *= 10}
    return kotlin.math.round(this * multipler) / multipler
}