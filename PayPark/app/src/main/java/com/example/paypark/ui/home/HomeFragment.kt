package com.example.paypark.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.paypark.R
import com.example.paypark.model.Parking
import com.example.paypark.viewmodels.ParkingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private val TAG = this.toString()
    private lateinit var parkingViewModel : ParkingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val fabAddParking: FloatingActionButton = root.findViewById(R.id.fabAddParking)
        fabAddParking.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_add_parking_fragment)
        }

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        parkingViewModel = ParkingViewModel()
    }

    override fun onResume() {
        super.onResume()

        parkingViewModel.getAllParkings()
    }
}