package com.example.paypark.ui.addparking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.paypark.R
import java.text.DateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddParkingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddParkingFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val TAG = this.toString()
    private lateinit var edtBuildingCode: EditText
    private lateinit var edtUnitNumber: EditText
    private lateinit var edtCarPlate: EditText
    private lateinit var edtParkingDate: EditText
    private lateinit var edtParkingLocation: EditText
    private lateinit var spnDuration: Spinner
    private lateinit var btnSave: Button

    private var selectedDuration: Long = 1
    private val durationValues: Array<Long> = arrayOf(4, 8, 12, 24)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_parking, container, false)

        edtBuildingCode = root.findViewById(R.id.edtBuildingCode)
        edtUnitNumber = root.findViewById(R.id.edtUnitNumber)
        edtCarPlate = root.findViewById(R.id.edtCarPlateNumber)
        edtParkingDate = root.findViewById(R.id.edtParkingDate)
        edtParkingDate.isFocusable = false
        edtParkingDate.setOnClickListener(this)

        edtParkingLocation = root.findViewById(R.id.edtParkingLocation)
        spnDuration = root.findViewById(R.id.spnDuration)
        btnSave = root.findViewById(R.id.btnSaveParking)
        btnSave.setOnClickListener(this)

        this.setCurrentDateTime()
        this.setUpSpinner()

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddParkingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddParkingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.edtParkingDate -> {
                    this.fetchDateTime()
                }
                R.id.btnSaveParking -> {
                    //TO-DO retrieve data and save parking to database
                }
            }
        }
    }

    private fun setUpSpinner(){
        val durationAdapter = ArrayAdapter(
            this.requireActivity(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.duration_array)
        )

        spnDuration.adapter = durationAdapter
        selectedDuration = this.durationValues[0]

        spnDuration.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedDuration = durationValues[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedDuration = durationValues[2]
            }
        }
    }

    private fun fetchDateTime() {
        val calendar= Calendar.getInstance()
        val year=calendar[Calendar.YEAR]
        val month=calendar[Calendar.MONTH]
        val day=calendar[Calendar.DAY_OF_MONTH]

        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.DAY_OF_MONTH]


        val datePickerDialog = DatePickerDialog(
            this.requireActivity(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
//                calendar.set(year, month, dayOfMonth)

                //TO-DO TimePickerDialog()
                TimePickerDialog(
                        this.requireActivity(),
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            calendar.set(year, month, dayOfMonth, hourOfDay, minute)

                            val parkingDate = calendar.time

//                https://developer.android.com/reference/java/text/DateFormat
                            val df: DateFormat =
                                    DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
                            edtParkingDate.setText(df.format(parkingDate).toString())
                        },
                        hour,
                        minute,
                        false
                ).show()
            },
                year,
                month,
                day
        )
        datePickerDialog.show()
    }

    private fun setCurrentDateTime(){
        val calendar = Calendar.getInstance()

        val df: DateFormat =
            DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
        edtParkingDate.setText(df.format(calendar.time).toString())
    }
}