package com.spezia.spezia.view.camerax

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.spezia.spezia.R
import com.spezia.spezia.databinding.ActivityCameraBinding
import com.spezia.spezia.view.scan.ScanActivity
import createFile

class CameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCameraBinding
    private var imgCapture : ImageCapture? = null
    private var camSelector : CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSwitchCam.setOnClickListener {
            camSelector = if (camSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
            launchCamera()
        }

        binding.btnShot.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val imgCapture = imgCapture ?: return
        val imgFile = createFile(application)
        val resultImgOptions = ImageCapture.OutputFileOptions.Builder(imgFile).build()

        imgCapture.takePicture(
            resultImgOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra("picture", imgFile)
                    intent.putExtra("isBackCamera", camSelector == CameraSelector.DEFAULT_BACK_CAMERA)

                    setResult(ScanActivity.CAMERA_RESULT_CODE, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        getString(R.string.failed_take_photo),
                        Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun launchCamera() {
        val camProviderFuture = ProcessCameraProvider.getInstance(this)

        camProviderFuture.addListener({
            val camProvider : ProcessCameraProvider = camProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewCamera.surfaceProvider)
                }

            imgCapture = ImageCapture.Builder().build()

            try {
                camProvider.unbindAll()
                camProvider.bindToLifecycle(
                    this,
                    camSelector,
                    preview,
                    imgCapture
                )
            } catch (exception : Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    getString(R.string.failed_show_camera),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onResume() {
        super.onResume()
        setupFullScreen()
        launchCamera()
    }

    private fun setupFullScreen() {
        supportActionBar?.hide()
    }

}