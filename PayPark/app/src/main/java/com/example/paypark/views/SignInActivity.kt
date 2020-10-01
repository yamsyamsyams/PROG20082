package com.example.paypark.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.paypark.R
import com.example.paypark.utils.DataValidations
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {
    val TAG : String = this@SignInActivity.toString()
    lateinit var tvCreateAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        tvCreateAccount = findViewById(R.id.tvCreateAccount)
        tvCreateAccount.setOnClickListener(this)
        btnSignIn.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        if(v!=null){
            if(v.id == tvCreateAccount.id){ // v? if v != null

                // go to SignUpActivity
                this.goToSignUp()
            }
            else if(v.id == btnSignIn.id){
                this.validateUser()
            }
        }
    }

    private fun validateData() : Boolean{
        if (edtEmail.text.isEmpty()){
            edtEmail.setError("Email cannot be empty")
            return false
        }

        if (edtPassword.text.isEmpty()){
            edtPassword.setError("Password cannot be empty")
            return false
        }

        if (!DataValidations().validateEmail(edtEmail.text.toString())){
            edtEmail.setError("Please provide valid email address")
            return false
        }

        return true
    }

    private fun validateUser(){
        if(edtEmail.text.toString().equals("test") && edtPassword.text.toString().equals("test")){
            this.goToMain()
        }
    }

    private fun goToMain(){
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)

        // to not allow user to go back to SignInActivity when they press bakc button on MainActivity
        // finishAffinity() will remove current activity from Activity Stack
        // along with all other activities below itself
        this@SignInActivity.finishAffinity()
    }

    private fun goToSignUp(){
        val signUpIntent = Intent(this, SignUpActivity::class.java)
        startActivity(signUpIntent)
    }
}