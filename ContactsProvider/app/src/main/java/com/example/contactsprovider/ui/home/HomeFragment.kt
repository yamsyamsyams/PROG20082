package com.example.contactsprovider.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsprovider.R
import com.example.contactsprovider.adapters.ContactsAdapter
import com.example.contactsprovider.model.Contact

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private val REQUEST_PERMISSION_CODE = 109
    private val REQUIRE_PERMISSIONS = arrayOf(android.Manifest.permission.READ_CONTACTS)
    private val TAG = "HomeFragment"

    private lateinit var contactsList : ArrayList<Contact>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: ContactsAdapter
    private lateinit var rvContacts: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        this.rvContacts = root.findViewById(R.id.rvContacts)
        this.contactsList = arrayListOf()

        this.viewAdapter = ContactsAdapter(this.requireActivity().applicationContext, this.contactsList)
        this.rvContacts.adapter = this.viewAdapter

        this.viewManager = LinearLayoutManager(this.requireContext())
        this.rvContacts.layoutManager = this.viewManager

        this.rvContacts.addItemDecoration(
            DividerItemDecoration(this.requireContext(), DividerItemDecoration.VERTICAL)
        )

        if(allPermissionsGranted()){
            this.fetchContacts()
        }
        else{
            ActivityCompat.requestPermissions(this.requireActivity(), REQUIRE_PERMISSIONS, REQUEST_PERMISSION_CODE)
        }
        return root
    }

    private fun allPermissionsGranted() : Boolean = REQUIRE_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(this.requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(allPermissionsGranted()){
                this.fetchContacts()
            }
            else{
                Log.e(TAG, "Permission denied to READ_CONTACTS")
            }
        }
    }

    fun fetchContacts(){
        homeViewModel.startFetchingContacts(this.requireActivity().applicationContext)

        homeViewModel.contactsList.observe(viewLifecycleOwner, {
            if( !it.isNullOrEmpty()){
                Log.e(TAG, it.toString())
                viewAdapter.contactList.clear()
                viewAdapter.contactList.addAll(it as ArrayList<Contact>)
                viewAdapter.notifyDataSetChanged()
            }
        })
    }
}