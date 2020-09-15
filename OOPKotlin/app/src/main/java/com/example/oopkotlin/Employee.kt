package com.example.oopkotlin

open class Employee (var name: String = "Unknown",
                     var earnings: Double = 0.0
//                var earnings: Double // if not initialized, do it with an init block
){
    //    init{
//        earnings = 10.0
//    }
    open fun display(){
        println("Employee name: " + this.name)
    }
}