package com.example.weding.activity.Activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.example.weding.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_feed.*
import java.util.*

class FeedActivity : AppCompatActivity() {
    var selected : Uri? = null
    var mAuth : FirebaseAuth?= null
    var mAuthListener : FirebaseAuth.AuthStateListener ?= null
    var firebaseDatabase : FirebaseDatabase? = null
    var myRef : DatabaseReference? = null
    var mStorageRef : StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        myRef = firebaseDatabase!!.reference
        mStorageRef = FirebaseStorage.getInstance().reference
    }
    fun upload(view: View){
        val uuid = UUID.randomUUID()
        val imageName = "images/$uuid.jpg"
        val storageReference = mStorageRef!!.child(imageName)
        storageReference.putFile(selected!!)
            .addOnSuccessListener { taskSnapshot ->
               val downloadURL = taskSnapshot.storage.downloadUrl.toString()
                  println(downloadURL)
            }
            .addOnFailureListener{ exception ->
                if (exception!= null){
                    Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }
            .addOnCompleteListener{ task ->
                if (task.isComplete){
                    Toast.makeText(applicationContext,"Post Shared", Toast.LENGTH_SHORT).show()

                }

            }
        Log.i("upload","Button Clicked")

    }

    fun selectImage(view: View){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
           requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }else{
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            selected = data.data

            try{
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selected)
                img_view.setImageBitmap(bitmap)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}