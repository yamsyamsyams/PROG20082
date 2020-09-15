package com.example.oopkotlin

//class Main {

//}

//var itsKotlin = Main()
//itsKotlin.main()

fun main(){
    println("Hello Kotlin !")

    var emp:Employee = Employee()
    emp.name = "JJ"
    emp.earnings = 100.0
    emp.display()

    var ftemp = FullTimeEmployee(ftName = "Jam", annualSalary = 50000.0)
    ftemp.display()
}