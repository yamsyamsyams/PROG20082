package com.profjk.booksinfojson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val TAG = this@MainActivity.toString()
    private lateinit var titles : Array<String>
    private lateinit var isbn : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.getInitialData()
        this.initializeSpinner()
    }

    private fun getInitialData(){
        titles  = arrayOf("Corinthians", "Niagara", "Blue Marble", "Psalm 23", "Thermal Conductivity", "India")
        isbn = arrayOf("0310570573", "0385673655", "9812474080", "1606475312", "1566764777", "0520221729")

        /*
        * Take home exercise - Augment the app to retrieve titles and isbn data from API
        * to populate the Spinner with initial values*/
    }

    private fun initializeSpinner(){
        val titlesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titles)
        titlesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTitles.adapter = titlesAdapter
        spnTitles.onItemSelectedListener = this@MainActivity
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        this.getBookInfo()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // nothing to do
    }

    private fun getBookInfo(){

    }
}