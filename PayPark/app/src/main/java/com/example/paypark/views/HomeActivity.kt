package com.example.paypark.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.example.paypark.R
import com.example.paypark.managers.SharedPreferencesManager
import com.example.paypark.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var userViewModel: UserViewModel

    private lateinit var navController: NavController
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgProfilePic: ImageView
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userViewModel = UserViewModel(this.application)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_support, R.id.nav_user_manual
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val headerLayout : View = navView.getHeaderView(0)
        tvEmail = headerLayout.findViewById(R.id.tvEmail)
        tvName = headerLayout.findViewById(R.id.tvName)
        imgProfilePic = headerLayout.findViewById(R.id.imgProfilePic)
        imgProfilePic.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_view_profile -> {
//                navController.navigate(R.id.action_nav_home_to_nav_profile)

                navController.navigate(R.id.nav_profile)
            }
            R.id.action_delete_account -> {
                val alertBuilder = AlertDialog.Builder(this)

                alertBuilder.setTitle("Sad to see you go")
                alertBuilder.setMessage("Are you sure you want to delete your account?")
                alertBuilder.setPositiveButton(android.R.string.yes){ dialog, which ->

                    this.deleteAccount()
                }

                alertBuilder.setNegativeButton(android.R.string.no){ dialog, which ->
                    Toast.makeText(this, "Thank you for staying with us.", Toast.LENGTH_LONG).show()
                }

//                alertBuilder.setNeutralButton(){}

                alertBuilder.show()
            }
            R.id.action_sign_out -> {
                this.finishAffinity()
                val signInIntent = Intent(this, SignInActivity::class.java)
                startActivity(signInIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteAccount(){
        val email = SharedPreferencesManager.read(SharedPreferencesManager.EMAIL, "")
        if (email != null) {
            userViewModel.deleteUserByEmail(email).apply {  }
        }

        this.finishAffinity()
        SharedPreferencesManager.removeAll()
        val signUpIntent = Intent(this, SignUpActivity::class.java)
        startActivity(signUpIntent)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.imgProfilePic -> {
                    val actionItems = arrayOf("Take a new picture", "Choose from gallery", "Cancel")
                    val alertBuilder = AlertDialog.Builder(this)
                    alertBuilder.setTitle("Select profile picture")
                    alertBuilder.setItems(actionItems){
                        dialog, index ->
                        if(actionItems.get(index).equals("Take a new picture")){
                            Log.e("HomeActivity", "Taking new picture")
                            this.navController.navigate(R.id.action_nav_home_to_fragment_camera_x)

                            // hide the drawer when we click on take a picture
                            this.drawerLayout.closeDrawer(Gravity.LEFT, true)
                        }
                        else if(actionItems.get(index).equals("Choose from gallery")){
                            Toast.makeText(this, "Choosing from gallery", Toast.LENGTH_SHORT).show()
                        }
                        else if(actionItems.get(index).equals("Cancel")){
                            dialog.dismiss()
                        }
                    }
                    alertBuilder.show()
                }
            }
        }
    }
}