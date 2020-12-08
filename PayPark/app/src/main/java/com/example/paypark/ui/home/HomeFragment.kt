package com.example.paypark.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paypark.R
import com.example.paypark.adapters.OnItemClickListener
import com.example.paypark.adapters.ReceiptsAdapter
import com.example.paypark.model.Parking
import com.example.paypark.viewmodels.ParkingViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : androidx.fragment.app.Fragment(), OnItemClickListener {

    private val TAG = this.toString()
    private lateinit var parkingViewModel: ParkingViewModel

    private lateinit var rvReceipts: RecyclerView
    private lateinit var viewAdapter: ReceiptsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var receiptsList: MutableList<Parking>

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val fabAddParking: FloatingActionButton = root.findViewById(R.id.fabAddParking)

        fabAddParking.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_add_parking_fragment)
        }

        this.rvReceipts = root.findViewById(R.id.rvReceipts)
        this.receiptsList = mutableListOf()

        this.viewAdapter = ReceiptsAdapter(this.requireContext(), this.receiptsList, this)
        this.rvReceipts.adapter = this.viewAdapter

        this.viewManager = LinearLayoutManager(this.requireContext())
        this.rvReceipts.layoutManager = this.viewManager

        this.rvReceipts.setHasFixedSize(true)
        this.rvReceipts.addItemDecoration(DividerItemDecoration(this.requireContext(), DividerItemDecoration.VERTICAL))

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parkingViewModel = ParkingViewModel()
    }

    override fun onResume() {
        super.onResume()

        parkingViewModel.getAllParkings()
        this.getReceiptList()
    }

    fun getReceiptList() {
        this.parkingViewModel.parkingList.observe(viewLifecycleOwner, { parkingList ->
            if (parkingList != null) {
                receiptsList.clear()
                receiptsList.addAll(parkingList)
                viewAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onItemClicked(receipt: Parking) {
        Toast.makeText(context, receipt.parkingDate.toString(), Toast.LENGTH_SHORT).show()
        //modify for displaying the fragment to display the parking details and allow the user to modify it.
    }

}