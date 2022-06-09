package com.spezia.spezia.view.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.spezia.spezia.R
import com.spezia.spezia.ml.SpiceModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScanActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var result: TextView? = null
    private var imageSize = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val camera = findViewById<Button>(R.id.button)
        val gallery = findViewById<Button>(R.id.button2)
        result = findViewById(R.id.result)
        imageView = findViewById(R.id.imageView)

        camera.setOnClickListener {
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkSelfPermission(
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            ) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 3)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
                }
            }
        }

        gallery.setOnClickListener {
            val cameraIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, 1)
        }
    }

    private fun classifyImage(image: Bitmap?) {
        try {
            val model = SpiceModel.newInstance(applicationContext)

            // Creates inputs for reference.
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imageSize * imageSize)
            image!!.getPixels(
                intValues,
                0,
                image.width,
                0, 0,
                image.width,
                image.height
            )
            var pixel = 0

            // Iterate over each pixel & extract RGB values. Add those values individually
            // to the byte buffer
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            // Find the index of the class with the biggest confidence
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf(
                "asam jawa", "cengkeh", "daun jeruk", "daun salam",
                "jahe", "kayu manis", "keluak", "kemiri", "ketumbar", "kunyit",
                "lada hitam", "pekak", "serai"
            )
            result!!.text = classes[maxPos]

            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) if (requestCode == 3) {
            var image: Bitmap? = null
            if (data != null) {
                image = data.extras!!["data"] as Bitmap?
            }
            var dimension = 0
            if (image != null) {
                dimension = image.width.coerceAtMost(image.height)
            }
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            imageView!!.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
            classifyImage(image)
        } else {
            var dat: Uri? = null
            if (data != null) dat = data.data
            var image: Bitmap? = null
            try {
                image = MediaStore.Images.Media.getBitmap(this.contentResolver, dat)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            imageView!!.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image!!, imageSize, imageSize, false)
            classifyImage(image)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object{
        const val EXTRA_TOKEN_SCAN = "extra_token_scan"
    }
}