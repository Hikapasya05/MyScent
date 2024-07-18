package com.hika.user.util

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraManager(
    private val activity: AppCompatActivity,
    private val onImageSelected: (Bitmap) -> Unit
) {

    private var currentPhotoPath: String = ""

    private val cameraActivityLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath).rotateImageIfRequired(currentPhotoPath)
            onImageSelected(imageBitmap)
        }
    }


    fun openCamera() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
                val photoFile = createImageFile()
                photoFile.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        activity,
                        "com.hika.user.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    cameraActivityLauncher.launch(takePictureIntent)
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun Bitmap.rotateImageIfRequired(path: String): Bitmap {
        val ei = ExifInterface(path)
        return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(270f)
            else -> this
        }
    }


    private fun Bitmap.rotateImage(angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
    }
}