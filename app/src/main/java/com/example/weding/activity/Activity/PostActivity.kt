package com.example.weding.activity.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.weding.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    val Video : Int = 0
    lateinit var uri : Uri
    lateinit var mStorage : StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        mStorage = FirebaseStorage.getInstance().getReference("Uploads")
        video.setOnClickListener(View.OnClickListener {
                view: View? ->  val intent = Intent()
            intent.setType("video/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select Video"), Video)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uriTxt = findViewById<View>(R.id.uriTxt) as TextView
        if (resultCode == RESULT_OK){
            if (requestCode == Video){
                uri = data!!.data!!
                uriTxt.text = uri.toString()
                upload()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upload() {
        var mReference = mStorage.child(uri.lastPathSegment!!)
        try {
            mReference.putFile(uri).addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                var url = taskSnapshot!!.uploadSessionUri
                val dwnTxt = findViewById<View>(R.id.dwnTxt) as TextView
                dwnTxt.text = url.toString()
                Toast.makeText(this, "Successfully Uploaded :", Toast.LENGTH_SHORT).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }

    }
}