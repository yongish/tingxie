package com.zhiyong.tingxie.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zhiyong.tingxie.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService

class CameraFragment : Fragment() {

  companion object {
    private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private const val TAG = "CameraFragment"
    private const val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    fun newInstance() = CameraFragment()
  }

  private var imageCapture: ImageCapture? = null
  private lateinit var outputDirectory: File
  private lateinit var cameraExecutor: ExecutorService
  private lateinit var btnCapture: Button
  private lateinit var viewFinder: PreviewView

  private lateinit var viewModel: CameraViewModel

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return inflater.inflate(R.layout.camera_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // Set up the listener for take photo button
    btnCapture = requireActivity().findViewById(R.id.camera_capture_button)
    btnCapture.setOnClickListener { takePhoto() }

    viewFinder = requireActivity().findViewById(R.id.viewFinder)

//    outputDirectory = getOutputDirectory()

    cameraExecutor = Executors.newSingleThreadExecutor()

    // Request camera permissions
    if (allPermissionsGranted()) {
      startCamera()
    } else {
      ActivityCompat.requestPermissions(
        requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
    // TODO: Use the ViewModel
  }

  private fun takePhoto() {
    // Get a stable reference of the modifiable image capture use case
    val imageCapture = imageCapture ?: return

    // Create time-stamped output file to hold the image
    val photoFile = File(
      outputDirectory,
      SimpleDateFormat(
        FILENAME_FORMAT, Locale.US
      ).format(System.currentTimeMillis()) + ".jpg"
    )

    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    // Set up image capture listener, which is triggered after photo has
    // been taken
    imageCapture.takePicture(
      outputOptions,
      ContextCompat.getMainExecutor(context),
      object : ImageCapture.OnImageSavedCallback {
        override fun onError(exc: ImageCaptureException) {
          Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
        }

        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
          val savedUri = Uri.fromFile(photoFile)
          val msg = "Photo capture succeeded: $savedUri"
          Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
          Log.d(TAG, msg)
        }
      })
  }

  private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

    cameraProviderFuture.addListener(Runnable {
      // Used to bind the lifecycle of cameras to the lifecycle owner
      val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

      // Preview
      val preview = Preview.Builder()
        .build()
        .also {
          it.setSurfaceProvider(viewFinder.surfaceProvider)
        }

      imageCapture = ImageCapture.Builder()
        .build()

      val imageAnalyzer = ImageAnalysis.Builder().build().also { it.setAnalyzer(cameraExecutor,
        { image ->
          run {
            // Draw boxes around words.
          }
        }) }

      // Select back camera as a default
      val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

      try {
        // Unbind use cases before rebinding
        cameraProvider.unbindAll()

        // Bind use cases to camera
        cameraProvider.bindToLifecycle(
          this, cameraSelector, preview, imageCapture, imageAnalyzer)

      } catch(exc: Exception) {
        Log.e(TAG, "Use case binding failed", exc)
      }

    }, ContextCompat.getMainExecutor(context))
  }

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
}
