package com.example.weding.activity.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.weding.R
import com.example.weding.activity.database.DatabaseHandler
import com.example.weding.activity.model.HappyPlaceModel
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_wedin.*


class WedinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wedin)

        supportActionBar?.hide()
        btn_scan.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
            if (result != null){
                if (result.contents==null){
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_SHORT).show()
                }
            } else{
                super.onActivityResult(requestCode, resultCode, data)
            }
        }

    }
}

