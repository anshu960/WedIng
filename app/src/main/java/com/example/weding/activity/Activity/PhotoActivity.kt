package com.example.weding.activity.Activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weding.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar
    private val VIDEO_PICK_GALLERY_CODE = 100
    private val IMAGE_PICK_GALLERY_CODE = 101
    private val VIDEO_PICK_CAMERA_CODE = 102
    private val  CAMERA_REQUEST_CODE = 103
    private lateinit var cameraPermission:Array<String>
    private lateinit var progressDialog: ProgressDialog
    private var videoUri: Uri? = null
   // private var imageUri: Uri? = null
    private var title: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        actionBar = supportActionBar!!
        actionBar.title = "New Post"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        cameraPermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Uploading Video...")
        progressDialog.setCanceledOnTouchOutside(false)


        uploadVideoBtn.setOnClickListener {
            title = titleEt.text.toString().trim()
            if (TextUtils.isEmpty(title)){
                Toast.makeText(this,"Title is required", Toast.LENGTH_SHORT).show()
            }
            else if (videoUri == null){
                Toast.makeText(this, "Pick the video", Toast.LENGTH_SHORT).show()
            }
            else{
                uploadVideoFirebase()
            }
        }
        pickVideo.setOnClickListener {
            videoPickDialog()
        }
    }

    private fun uploadVideoFirebase() {
        progressDialog.show()
        val timestamp = System.currentTimeMillis()
        val filepath = "Videos/video_$timestamp"
        val storageReference = FirebaseStorage.getInstance().getReference(filepath)
        storageReference.putFile(videoUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful){
                    val downloadUri = uriTask.result
                    if (uriTask.isSuccessful){
                        val hashMap = HashMap<String, Any>()
                        hashMap["id"] = "$timestamp"
                        hashMap["title"] = "$title"
                        hashMap["timestamp"] = "$timestamp"
                        hashMap["videoUri"] = "$downloadUri"

                        val dbReference = FirebaseDatabase.getInstance().getReference("Video")
                        dbReference.child(timestamp.toString())
                            .setValue(hashMap)
                            .addOnSuccessListener {
                                progressDialog.dismiss()
                                Toast.makeText(this,"Video Uploaded", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e->
                                progressDialog.dismiss()
                                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()

                            }
                    }
                }

            }
            .addOnFailureListener({ e->
                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            })
    }

    private fun setVideoToVideoView() {
       val mediaController = MediaController(this)
        mediaController.setAnchorView(img_view)
        img_view.setMediaController(mediaController)
        img_view.setVideoURI(videoUri)
        img_view.requestFocus()
        img_view.setOnPreparedListener {
            img_view.pause()
        }
    }
    private fun videoPickDialog() {
       val options = arrayOf("Image","Video")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Video From")
            .setItems(options){ dialogInterface, i->
                if (i == 0){
                    if (!checkCameraPermission()){
                       requestCameraPermission()
                    }
                    else{
                        videoPickCamera()
                    }

                }
                else{
                    videoPickGallery()
                }

            }
            .show()
    }
    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(
            this,
            cameraPermission,
            CAMERA_REQUEST_CODE
        )
    }
    private fun checkCameraPermission():Boolean{
        val result1 = ContextCompat.checkSelfPermission(this,
        Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val result2 = ContextCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
        )== PackageManager.PERMISSION_GRANTED
        return result1 && result2
    }
    private fun videoPickGallery(){
        val intent = Intent()
          intent.type = "video/*"
       // intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Choose Video"),
            VIDEO_PICK_GALLERY_CODE
        )

    }
    private fun videoPickCamera(){
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_PICK_CAMERA_CODE)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CAMERA_REQUEST_CODE ->
                if (grantResults.size > 0){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storage){
                        videoPickCamera()
                    }
                    else{
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK){
            if (requestCode == VIDEO_PICK_CAMERA_CODE){
                videoUri == data!!.data
                setVideoToVideoView()
            }
            else if (requestCode == VIDEO_PICK_GALLERY_CODE){
               videoUri = data!!.data
               setVideoToVideoView()
            }
        }
        else{

        }

        super.onActivityResult(requestCode, resultCode, data)
    }


}
