package com.example.paypark.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.paypark.R
import com.example.paypark.managers.SharedPreferencesManager
import com.example.paypark.model.User
import com.example.paypark.utils.DataValidations
import com.example.paypark.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {
    val TAG : String = this@SignUpActivity.toString()
    var selectedGender: String = ""

    //python equivalent @staticmethod
    companion object{
        var user = User()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        this.initialSetup()
        this.initializeSpinner()
        selectedGender = resources.getStringArray(R.array.gender_array).get(0)

        spnGender.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGender = resources.getStringArray(R.array.gender_array).get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedGender = resources.getStringArray(R.array.gender_array).get(2)
            }

        }

        btnSignUp.setOnClickListener(this)

        edtExpiry.isFocusable = false
        edtExpiry.setOnClickListener(this) //display the datePicker
    }

    fun initializeSpinner(){
        val genderAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.gender_array))

        spnGender.adapter = genderAdapter
    }

    fun initialSetup(){
        edtName.setAutofillHints(View.AUTOFILL_HINT_NAME)
        edtEmail.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS)
//        edtCreditCardNum.setAutofillHints() //avoid for security reasons
        edtNameOnCard.setAutofillHints(View.AUTOFILL_HINT_NAME)
        edtPhoneNumber.setAutofillHints(View.AUTOFILL_HINT_PHONE)

        //the hardcoded values for each of these inputs
        // are just used to save time when we are testing this activity
        edtName.setText("Jigisha Patel")
        edtPhoneNumber.setText("1234567890")
        edtPassword.setText("jk123")
        edtConfirmPassword.setText("jk123")
        edtCvv.setText("123")
        edtCarPlateNumber.setText("abcd123")
        edtEmail.setText("jk@jk.com")
        edtNameOnCard.setText("JK")
        edtCreditCardNum.setText("1234123412341234")
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                btnSignUp.id -> {
                    //gather all the data and create object of User class
                    if (this.validateData()) {
                        this.fetchData()
                        this.saveUserToDB()

                        SharedPreferencesManager.write(SharedPreferencesManager.EMAIL, edtEmail.text.toString())

                        this.goToMain()
                    }
                }
                edtExpiry.id -> {
                    //show the fragment for Date Picker

                    val dpFragment = DatePickerFragment()
                    //tag is used to identify fragments
                    dpFragment.show(supportFragmentManager, "datepicker")
                }
            }
        }
    }

    fun saveUserToDB(){
        try{
            var userViewModel = UserViewModel(this.application)
            userViewModel.insertAll(user)
        }catch (ex: Exception){
            Log.e(TAG, ex.toString())
            Log.e(TAG, ex.localizedMessage)
        }
    }

    fun fetchData(){
        user.name = edtName.text.toString()
        user.email = edtEmail.text.toString()
        user.phoneNumber = edtPhoneNumber.text.toString()
        user.carPlate = edtCarPlateNumber.text.toString()
        user.creditCardNumber = edtCreditCardNum.text.toString()
        user.nameOnCard = edtNameOnCard.text.toString()

        if (edtCvv.text.toString().isNotEmpty()) {
            user.cvv = edtCvv.text.toString().toInt()
        }
        user.gender = selectedGender
        user.password = DataValidations().encryptPassword(edtPassword.text.toString())

        Log.d(TAG, "User : " + user.toString())
    }

    fun goToMain(){
        //open MainActivity and pass the user object
//        val mainIntent = Intent(this, MainActivity::class.java)
//        startActivity(mainIntent)

        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)

        //remove the SignUpActivity from teh activity stack
        this@SignUpActivity.finishAffinity()
        //finishAndRemoveTask()
        //finish()
    }

    fun validateData() : Boolean{
        if (edtName.text.toString().isEmpty()){
            edtName.error = "Please enter name"
            return false
        }

        if (edtEmail.text.toString().isEmpty()){
            edtEmail.error = "Email cannot be empty"
            return false
        }else if(!DataValidations().validateEmail(edtEmail.text.toString())){
            edtEmail.error = "Please provide valid email address"
            return false
        }

        if (edtPassword.text.toString().isEmpty()){
            edtPassword.error = getString(R.string.error_empty_password)
            return false
        }

        if (edtConfirmPassword.text.toString().isEmpty()){
            edtConfirmPassword.error = "Confirm password cannot be empty"
            return false
        }

        if (!edtPassword.text.toString().equals(edtConfirmPassword.text.toString())){
            edtPassword.error = "Both passwords must be same"
            edtConfirmPassword.error = "Both passwords must be same"
            return false
        }
        //take home - add errors and data validations for the remaining inputs

        return true
    }

    //removed inner keyword from class declaration
    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener{
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //use the current date as the default date for the date picker
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(this.requireActivity(), this, year, month, day )

        }

        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            //TODO for the operation to be performed on the date selected by the user

            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val expiryDate = calendar.time

            user.expiryDate = expiryDate

            val sdf = SimpleDateFormat("MM/YY")
            this.requireActivity().edtExpiry.setText(sdf.format(expiryDate).toString())
        }

    }
}