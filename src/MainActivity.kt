package com.example.humanfaceclassifier

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var currentPath: String? = null
    val TAKE_PICTURE = 1
    val SELECT_PICTURE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gallery_button.setOnClickListener {
            dispatchGalleryIntent()
        }
        camera_button.setOnClickListener {
            dispatchCameraIntent()
        }
        about_button.setOnClickListener {
            openActivity("AboutActivity")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val file = File(currentPath!!)
                val uri = Uri.fromFile(file)
                Log.d("TAKE_PICTURE", "got uri")
                openCropActivity(uri!!, uri)
                val resultUri = UCrop.getOutput(data!!)
                main_photo.setImageURI(uri)
                //imageView2.setImageBitmap(BitmapFactory.decodeFile(resultUri?.path))
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }

        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                Log.d("SELECT_PICTURE", "got uri")
                openCropActivity(uri!!, uri)
                val resultUri = UCrop.getOutput(data)
                main_photo.setImageURI(uri)
                //imageView2.setImageBitmap(BitmapFactory.decodeFile(resultUri?.path))
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = Uri.parse(currentPath)
                openCropActivity(uri, uri)
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }

        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val uri = UCrop.getOutput(data!!)
            imageView2.setImageURI(uri)
        }

        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                imageView2.setImageURI(uri)
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }*/

    fun dispatchGalleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_PICTURE)
    }

    fun dispatchCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImage()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (photoFile != null) {
                //
                val photoUri = FileProvider.getUriForFile(this,
                    "com.example.humanfaceclassifier.fileprovider",
                    photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, TAKE_PICTURE)
            }
        }
    }

    fun createImage(): File {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").format(Date())
        val imageName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageName, ".jpg", storageDir)
        currentPath = image.absolutePath
        Log.d("createImage", "got image")
        return image
    }

    fun openCropActivity(sourceUri: Uri, destinationUri: Uri) {
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .start(this)
        Log.d("openCropActivity", "got crop activity")
    }

    fun openActivity(activityName: String) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }
}
