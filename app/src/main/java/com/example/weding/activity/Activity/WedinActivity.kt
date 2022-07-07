package com.example.weding.activity.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weding.R
import com.example.weding.activity.Adapters.AdapterRecord
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
    private fun setupHappyPlacesRecyclerview(happyPlaceList: ArrayList<HappyPlaceModel>){
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val placesAdapter = AdapterRecord(this, happyPlaceList)
        recyclerView.adapter = placesAdapter

    }
    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList : ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()
        if (getHappyPlaceList.size > 0){
            recyclerView.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupHappyPlacesRecyclerview(getHappyPlaceList)
        }else{
            recyclerView.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE

        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_SHORT).show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    }

