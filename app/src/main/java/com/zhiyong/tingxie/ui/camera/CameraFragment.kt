package com.zhiyong.tingxie.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zhiyong.tingxie.R
import kotlinx.android.synthetic.main.camera_fragment.*
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService

class CameraFragment : Fragment() {

  companion object {
    private const val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    fun newInstance() = CameraFragment()
  }

  private lateinit var outputDirectory: File
  private lateinit var cameraExecutor: ExecutorService

  private lateinit var viewModel: CameraViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Request camera permissions
    if (allPermissionsGranted()) {
      startCamera()
    } else {
      ActivityCompat.requestPermissions(
        requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    // Set up the listener for take photo button
    camera_capture_button.setOnClickListener { takePhoto() }

//    outputDirectory = getOutputDirectory()

    cameraExecutor = Executors.newSingleThreadExecutor()

    return inflater.inflate(R.layout.camera_fragment, container, false)
  }

  private fun takePhoto() {}

  private fun startCamera() {}

  private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(
      requireContext(), it) == PackageManager.PERMISSION_GRANTED
  }

//  private fun getOutputDirectory(): File {
//    val mediaDir = externalMediaDirs.firstOrNull()?.let {
//      File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
//    return if (mediaDir != null && mediaDir.exists())
//      mediaDir else filesDir
//  }

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<String>, grantResults:
    IntArray) {
    if (requestCode == REQUEST_CODE_PERMISSIONS) {
      if (allPermissionsGranted()) {
        startCamera()
      } else {
        Toast.makeText(context,
          "Permissions not granted by the user.",
          Toast.LENGTH_SHORT).show()
//        finish()
      }
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
    // TODO: Use the ViewModel
  }

}