package com.profjk.booksinfojson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.profjk.booksinfojson.network.Book
import com.profjk.booksinfojson.viewmodel.BookViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val TAG = this@MainActivity.toString()
    private lateinit var titles: Array<String>
    private lateinit var isbn: Array<String>
    private lateinit var bookViewModel: BookViewModel
    private var baseUrl = "https://www.googleapis.com/books/v1/volumes?q="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookViewModel = BookViewModel()

        this.getInitialData()
        this.initializeSpinner()
    }

    private fun getInitialData() {
        titles = arrayOf(
            "Corinthians",
            "Niagara",
            "Blue Marble",
            "Psalm 23",
            "Thermal Conductivity",
            "India"
        )
        isbn = arrayOf(
            "0310570573",
            "0385673655",
            "9812474080",
            "1606475312",
            "1566764777",
            "0520221729"
        )

        /*
        * Take home exercise - Augment the app to retrieve titles and isbn data from API
        * to populate the Spinner with initial values*/
    }

    private fun initializeSpinner() {
        val titlesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titles)
        titlesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnTitles.adapter = titlesAdapter
        spnTitles.onItemSelectedListener = this@MainActivity
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        this.getBookInfo(isbn[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // nothing to do
    }

    private fun getBookInfo(isbn: String) {
        val apiUrl = this.baseUrl + "isbn:${isbn}"
        Log.e(TAG, "apiUrl : " + apiUrl)

        this.bookViewModel.getBookInfo(apiUrl)
        this.bookViewModel.response.observe(this, {
            Log.e(TAG, "book response : " + it.toString())
            this.displaybookData(it)
        })
    }

    private fun displaybookData(book: Book) {
        if (book.bookItem != null) {
            with(book.bookItem) {
                if (this[0].volumeInfo != null) {
                    with(this[0].volumeInfo!!) {
                        if (this.authors[0] != null) {
                            tvAuthors.setText(this.authors[0].toString())
                            // take home - write the loop
                        } else {
                            tvAuthors.setText("Unavailable")
                        }
                    }

                    // take home - display rest of the values with appropriate checking
                }
                if (this[0].saleInfo != null) {
                    with(this[0].saleInfo!!) {
                        if (this.retailPrice != null) {
                            tvPrice.setText(this.retailPrice.price.toString() + " " + this.retailPrice.currencyCode)
                        } else {
                            tvPrice.setText("Unavailable")
                        }
                    }
                }
            }
        }
    }
}