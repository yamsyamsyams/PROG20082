package com.example.paypark.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import com.example.paypark.R
import com.example.paypark.managers.SharedPreferencesManager
import com.example.paypark.model.User
import com.example.paypark.viewmodels.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(), View.OnClickListener {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var edtName: EditText
    lateinit var edtCarPlateNumber: EditText
    lateinit var edtPhoneNumber: EditText
    lateinit var edtCreditCardNum:EditText
    lateinit var edtCvv: EditText
    lateinit var edtExpiry: EditText
    lateinit var edtNameOnCard: EditText
    lateinit var btnSave: Button
    lateinit var fabEditProfile: FloatingActionButton


    lateinit var userViewModel: UserViewModel
    lateinit var existingUser: User
    var currentUserEmail = SharedPreferencesManager.read(SharedPreferencesManager.EMAIL, "")
   // var currentUserEmail = "jh3@jh.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        userViewModel = UserViewModel(this.requireActivity().application)

        this.populateProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        root.fabEditProfile.setOnClickListener(this)
        root.btnSave.setOnClickListener(this)
        root.edtExpiry.isFocusable = false
        root.edtExpiry.setOnClickListener(this)

        edtName = root.edtName
        edtCarPlateNumber = root.edtCarPlateNumber
        edtNameOnCard = root.edtNameOnCard
        edtExpiry = root.edtExpiry
        edtCreditCardNum = root.edtCreditCardNum
        edtCvv = root.edtCvv
        edtPhoneNumber = root.edtPhoneNumber

        btnSave = root.btnSave
        fabEditProfile = root.fabEditProfile

        this.disableEdit()
       // this.populateProfile()

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun disableEdit(){
        edtName.isEnabled = false
        edtCarPlateNumber.isEnabled = false
        edtCreditCardNum.isEnabled = false
        edtCvv.isEnabled = false
        edtNameOnCard.isEnabled = false
        edtExpiry.isEnabled = false
        edtPhoneNumber.isEnabled = false
    }

    fun enableEdit(){
        edtName.isEnabled = true
        edtCarPlateNumber.isEnabled = true
        edtCreditCardNum.isEnabled = true
        edtCvv.isEnabled = true
        edtNameOnCard.isEnabled = true
        edtExpiry.isEnabled = true
        edtPhoneNumber.isEnabled = true
    }

    fun populateProfile(){
//        Log.e("ProfileFragment", "populateProfile() executing")

        if (currentUserEmail != null){
            Log.e("ProfileFragment", "currentUserEmail " + currentUserEmail)
            userViewModel.getUserByEmail(currentUserEmail!!)?.observe(this.requireActivity(), {matchedUser ->

//                Log.e("Profile Fragment", "Matched user : " + matchedUser.phoneNumber)

                if (matchedUser != null) {

                    this.existingUser = matchedUser

//                    Log.d("Profile Fragment", "Matched user : " + matchedUser.toString())

                    edtName.setText(matchedUser.name)
                    edtPhoneNumber.setText(matchedUser.phoneNumber)
                    edtCarPlateNumber.setText(matchedUser.carPlate)
                    edtCreditCardNum.setText(matchedUser.creditCardNumber)
                    edtNameOnCard.setText(matchedUser.nameOnCard)
                    edtCvv.setText(matchedUser.cvv.toString())
                    edtExpiry.setText(SimpleDateFormat("MM/YY").format(matchedUser.expiryDate).toString())
                }
            })
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            fabEditProfile.id -> {
                this.enableEdit()
                fabEditProfile.visibility = View.GONE
                btnSave.visibility = View.VISIBLE
            }
            btnSave.id -> {
                this.disableEdit()
                fabEditProfile.visibility = View.VISIBLE
                btnSave.visibility = View.GONE

                this.saveToDB()
            }
            edtExpiry.id -> {
                this.fetchDate()
            }
        }
    }

    private fun saveToDB(){
        this.existingUser.name = edtName.text.toString()
        this.existingUser.phoneNumber = edtPhoneNumber.text.toString()
        this.existingUser.carPlate = edtCarPlateNumber.text.toString()
        this.existingUser.creditCardNumber = edtCreditCardNum.text.toString()
        this.existingUser.nameOnCard = edtNameOnCard.text.toString()

        this.existingUser.cvv = edtCvv.text.toString().toInt()

        try{
            userViewModel.updateUser(existingUser)
        }catch (ex: Exception){
            Log.d("Profile Fragment", ex.localizedMessage)
        }
    }

    private fun fetchDate(){
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(this.requireActivity(),
            DatePickerDialog.OnDateSetListener{ view, year, month, dayOfMonth ->
                val sdf = SimpleDateFormat("MM/YY")
                calendar.set(year, month, dayOfMonth)

                val expiryDate = calendar.time
                edtExpiry.setText(sdf.format(expiryDate).toString())

                existingUser.expiryDate = expiryDate
            }, year, month, day)

        datePickerDialog.datePicker.minDate = Date().time
        datePickerDialog.show()
    }
}