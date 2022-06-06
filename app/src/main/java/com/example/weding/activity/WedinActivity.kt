package com.example.weding.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weding.R
import com.example.weding.activity.database.DataEvent
import com.example.weding.activity.database.DatabaseHandler
import com.example.weding.activity.database.SqliteDatabase
import kotlinx.android.synthetic.main.activity_wedin.*


class WedinActivity : AppCompatActivity() {
    private var clicked = false
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wedin)

        supportActionBar!!.hide()

        edit_floating_button.setOnClickListener {
            val intent = Intent(this, ContentActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_CODE)
        }
        getEventListDB()

        Add_btn.setOnClickListener {
            onAddButtonClicked()
        }
       /* edit_floating_button.setOnClickListener {
            startActivity(Intent(this, ContentActivity::class.java))
        }*/

    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            edit_floating_button.visibility = View.VISIBLE
        } else {
            edit_floating_button.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            edit_floating_button.startAnimation(fromBottom)
            Add_btn.startAnimation(rotateOpen)
        } else {
            edit_floating_button.startAnimation(toBottom)
            Add_btn.startAnimation(rotateClose)
        }
    }
    private fun setupEventRecycler(eventList: ArrayList<SqliteDatabase>){
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val placeAdapter = EventAdapter(this, eventList)
        recyclerView.adapter = placeAdapter
    }
   private fun getEventListDB(){
       val dbHandler = DatabaseHandler(this)
       val getEventList : ArrayList<SqliteDatabase> = dbHandler.getEventList()

       if (getEventList.size > 0){
           recyclerView.visibility = View.VISIBLE
           tv_no_record_available.visibility = View.GONE
           setupEventRecycler(getEventList)
           }else{
               recyclerView.visibility = View.GONE
               tv_no_record_available.visibility = View.VISIBLE
       }
   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLACE_ACTIVITY_CODE){
            if (resultCode == Activity.RESULT_OK){
                getEventListDB()
            }else{
                Log.e("Activity", "Cancelled")
            }
        }
    }
    companion object{
        var ADD_PLACE_ACTIVITY_CODE = 1
    }
}











