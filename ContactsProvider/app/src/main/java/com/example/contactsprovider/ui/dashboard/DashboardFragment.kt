package com.example.contactsprovider.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.contactsprovider.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var webView: WebView
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.INTERNET)
    private val REQUIRED_INTERNET_PERMISSION_CODE = 110

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        this.webView = root.findViewById(R.id.webView)

        if (allPermissionGranted()) {
            this.loadWebPage()
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUIRED_INTERNET_PERMISSION_CODE
            )
        }

        return root
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUIRED_INTERNET_PERMISSION_CODE) {
            if (allPermissionGranted()) {
                this.loadWebPage()
            } else {
                Log.e("DashboardFragment", "Cannot load the webpage")
            }
        }
    }

    private fun loadWebPage() {
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Log.e("DashboardFragment", error!!.description.toString())
                super.onReceivedError(view, request, error)
            }
        }

        webView.loadUrl("https://www.google.com/search?q=SheridanCollege")
    }
}