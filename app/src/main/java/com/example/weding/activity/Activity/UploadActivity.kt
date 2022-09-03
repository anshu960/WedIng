package com.example.weding.activity.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weding.R
import com.example.weding.activity.Activity.adapter.AdapterVideo
import com.example.weding.activity.Activity.model.ModelVideo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_upload.*

class UploadActivity : AppCompatActivity() {
    private lateinit var videoArrayList: ArrayList<ModelVideo>
    private lateinit var adapterVideo: AdapterVideo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        title = "Feeds"
        loadVideoFromFirebase()
    }

    private fun loadVideoFromFirebase() {
        videoArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Videos")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                videoArrayList.clear()
                for (ds in snapshot.children){
                    val modelVideo = ds.getValue(ModelVideo::class.java)
                    videoArrayList.add(modelVideo!!)
                }
                adapterVideo = AdapterVideo(this@UploadActivity, videoArrayList)
                recycler_upload.adapter = adapterVideo
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}