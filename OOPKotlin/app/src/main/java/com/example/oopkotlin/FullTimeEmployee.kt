package com.example.oopkotlin

class FullTimeEmployee(
    ftName: String = "Unknown",
    var annualSalary: Double? // nullable
) : Employee(ftName){
    init{
        earnings = annualSalary!! / 26
    }
    override fun display(){
        super.display() // if you want to override the display func, need to set the super func to open
        println("Annual salary $" + this.annualSalary)
        println(this.name + " earns $" + this.earnings.round(2) + " biweekly.")
    }
}

// extension
fun Double.round(decimals: Int): Double{
    var multiplier = 1.0
    repeat(decimals){multiplier *=10}
    return kotlin.math.round(this * multiplier) / multiplier
}