package com.example.paypark.ui.camera

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.paypark.R
import kotlinx.android.synthetic.main.fragment_camera_x.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.File
import java.io.IOException
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraXFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraXFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var btnCameraCapture: ImageButton
    private val REQUIRED_PERMISSIONS =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        if (allPermissionsGranted()) {
            // good to go further
            this.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_PERMISSION_CODE
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_camera_x, container, false)
        this.btnCameraCapture = root.findViewById(R.id.btnCameraCapture)
        this.btnCameraCapture.setOnClickListener(this)
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CameraXFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CameraXFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val REQUEST_PERMISSION_CODE = 171
        private const val TAG = "CameraXFragment"
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
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
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (allPermissionsGranted()) {
                // good to go
                this.startCamera()
            } else {
                Toast.makeText(
                    this.requireActivity(),
                    "Operation cannot be performed without camera access permission.",
                    Toast.LENGTH_SHORT
                ).show()
                this.findNavController().navigateUp()
            }
        }
    }

    private fun startCamera() {
        this.imageCapture = ImageCapture.Builder().build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            this.bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this.requireActivity()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(preview_view_container.surfaceProvider)
        }
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (ex: Exception) {
            Log.e(TAG, "Use case binding failed", ex)
        }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnCameraCapture -> {
                    this.takePhoto()
                }
            }
        }
    }

    private fun takePhoto() {
        if (this.imageCapture != null) {
            val outputDirectory = getOutputDirectory()
            val filename =
                "PayPark_" + SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.CANADA).format(
                    System.currentTimeMillis()
                ) + ".jpeg"
            val pictureFile = File(outputDirectory, filename)
            val outputOptions = ImageCapture.OutputFileOptions.Builder(pictureFile).build()
            imageCapture!!.takePicture(outputOptions,
                ContextCompat.getMainExecutor(this.requireActivity()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exception: ImageCaptureException) {
                        Log.e(TAG, "Image saving failed ${exception.message}", exception)
                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val pictureURI = Uri.fromFile(pictureFile)
                        Log.e(TAG, "Image successfully saved at ${pictureURI}")
                        this@CameraXFragment.requireActivity().imgProfilePic.setImageURI(pictureURI)
                        this@CameraXFragment.saveToExternalStorage(pictureURI)
                        this@CameraXFragment.findNavController().navigateUp()

                        // try to save this to Firebase cloud, then retrieve toof
                    }
                }
            )
        } else {
            Log.e(TAG, "ImageCapture use case is unavailable. Cannot click pictures.")
            return
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = this.requireActivity().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else this.requireActivity().filesDir
    }

    private fun saveToExternalStorage(pictureURI: Uri){
        if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()){
            val resolver: ContentResolver = this@CameraXFragment.requireActivity().applicationContext.contentResolver
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, pictureURI.lastPathSegment)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/PayPark")

            val uri = resolver.insert(contentUri, contentValues)

            if(uri == null){
                throw IOException("Failed to create media on external storage.")
            }
            else{
                Log.e(TAG, "Media successfully saved to external storage.")
            }
        }
        else{
            Log.e(TAG, "Unable to access external storage")
        }
    }

}