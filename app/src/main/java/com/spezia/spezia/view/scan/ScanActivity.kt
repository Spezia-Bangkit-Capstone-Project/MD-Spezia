package com.spezia.spezia.view.scan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.spezia.spezia.R
import com.spezia.spezia.api.api_responses.scan.ScanSpicesApiModel
import com.spezia.spezia.api.api_responses.scan.ScanSpicesResponse
import com.spezia.spezia.api.configuration.ApiConfig
import com.spezia.spezia.databinding.ActivityScanBinding
import com.spezia.spezia.view.camerax.CameraActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import reduceFileImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rotateBitmap
import uriToFile
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ScanActivity : AppCompatActivity() {

    private var binding : ActivityScanBinding? = null
    private lateinit var token : String
    private var getImgFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        token = intent.getStringExtra(EXTRA_TOKEN_SCAN).toString()
        val bundle = Bundle()
        bundle.putString(EXTRA_TOKEN_SCAN, token)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        Log.d("MyToken : ", token)

        setupButtonClickAction()
        setupGrantedPermissions()
        setupActionBarIconClickAction()
    }

    private fun setupActionBarIconClickAction() {
        binding?.backIcon?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupGrantedPermissions() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun setupButtonClickAction() {
        binding?.btnPhotoCamera?.setOnClickListener {
            startCamera()
        }
        binding?.btnPhotoGallery?.setOnClickListener {
            startGallery()
        }
        binding?.btnScanNow?.setOnClickListener {
            runScanNow()
        }
    }

    private fun startGallery() {
        val intentGallery = Intent(Intent.ACTION_GET_CONTENT)
        intentGallery.type = "image/*"
        val choosePict = Intent.createChooser(intentGallery, "Choose a Picture")
        launcherIntentGallery.launch(choosePict)
    }

    private fun runScanNow(){
        if (getImgFile != null) {
            showIndicatorScanLoadingProcess(true)

            val file = reduceFileImage(getImgFile as File)
            val reqImgFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imgMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                reqImgFile
            )

            val service = ApiConfig.getApiService().scanSpices("Bearer $token", imgMultipart)

            service.enqueue(object : Callback<ScanSpicesResponse> {
                override fun onResponse(
                    call: Call<ScanSpicesResponse>,
                    response: Response<ScanSpicesResponse>
                ) {
                    val respBody = response.body()
                    if (response.isSuccessful) {
                        if ((respBody != null) && !respBody.error) {
                            showIndicatorScanLoadingProcess(false)

                            binding?.btnViewDetails?.visibility = View.VISIBLE
                            binding?.apply {
                                tvOutputResult.text = respBody.predictionResult.name
                                Toast.makeText(
                                    this@ScanActivity,
                                    getString(R.string.accuracy,
                                        respBody.predictionResult.accuracy),
                                    Toast.LENGTH_LONG).show()

                                val scanMetadata = ScanSpicesApiModel(
                                    respBody.predictionResult.accuracy,
                                    respBody.predictionResult.spiceId,
                                    respBody.predictionResult.name,
                                    respBody.predictionResult.latinName,
                                    respBody.predictionResult.image,
                                    respBody.predictionResult.description,
                                    respBody.predictionResult.benefits
                                )
                                Log.d("ScanSpicesModel : ", scanMetadata.toString())

                                btnViewDetails.setOnClickListener {
                                    val intentWithData = Intent(
                                        applicationContext, SpiceScanDetailsActivity::class.java)
                                    intentWithData.let {
                                        it.putExtra(
                                            SpiceScanDetailsActivity.
                                            EXTRA_SPICES_SCAN_DETAILS,
                                            respBody.predictionResult)
                                        startActivity(it)
                                    }
                                }
                            }
                        }
                    } else {
                        showIndicatorScanLoadingProcess(false)
                        Toast.makeText(this@ScanActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ScanSpicesResponse>, t: Throwable) {
                    showIndicatorScanLoadingProcess(false)
                    Toast.makeText(this@ScanActivity, getString(R.string.api_message_error_on_server), Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            showIndicatorScanLoadingProcess(false)
            Toast.makeText(this, getString(R.string.image_story_empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera(){
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myImgFile = uriToFile(selectedImg, this)
            getImgFile = myImgFile
            binding?.ivDisplayImage?.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT_CODE) {
            val myImgFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getImgFile = myImgFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getImgFile?.path),
                isBackCamera
            )
            binding?.ivDisplayImage?.setImageBitmap(result)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.failed_get_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun showIndicatorScanLoadingProcess(lp:Boolean) {
        if (lp){
            binding?.whiteCircleBgScan?.visibility = View.VISIBLE
            binding?.loadingIndicatorScan?.visibility = View.VISIBLE
            binding?.btnPhotoCamera?.isEnabled = false
            binding?.btnPhotoGallery?.isEnabled = false
            binding?.btnScanNow?.isEnabled = false
        } else {
            binding?.whiteCircleBgScan?.visibility = View.GONE
            binding?.loadingIndicatorScan?.visibility = View.GONE
            binding?.btnPhotoCamera?.isEnabled = true
            binding?.btnPhotoGallery?.isEnabled = true
            binding?.btnScanNow?.isEnabled = true
        }
    }

    companion object{
        const val EXTRA_TOKEN_SCAN = "extra_token_scan"
        const val CAMERA_RESULT_CODE = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}