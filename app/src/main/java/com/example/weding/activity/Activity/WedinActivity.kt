package com.example.weding.activity.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weding.R
import com.example.weding.activity.database.DatabaseHandler
import com.example.weding.activity.model.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_wedin.*


class WedinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wedin)

        supportActionBar?.hide()
        Add_btn.setOnClickListener {
            val intent = Intent(this, ContentActivity::class.java)
            startActivity(intent)
        }
        getHappyPlacesListFromLocalDB()
    }
    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList : ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()
        if (getHappyPlaceList.size > 0){
            for (i in getHappyPlaceList){
                Log.e("Title", i.title)
                Log.e("Description", i.description)
            }
        }
    }
}

