package com.example.weding.activity.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weding.R
import com.example.weding.activity.Activity.Database.DatabaseHandler
import com.example.weding.activity.Activity.adapter.HappyPlacesAdapter
import com.example.weding.activity.Activity.model.HappyPlaceModel
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_wedin.*
import kotlinx.android.synthetic.main.nav_header.*

class WedinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wedin)
        supportActionBar?.hide()
        navDraw.itemIconTintList = null
        menu_img.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        navDraw.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item1 -> {
                    val intent = Intent(this,FeedActivity::class.java)
                    startActivity(intent)
                }
                R.id.item2 -> {
                    val intent = Intent(this,InvitationActivity::class.java)
                    startActivity(intent)
                }
                R.id.item3 -> {
                    val intent = Intent(this,PhotoActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        btn_scan.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }
        Add_btn.setOnClickListener {
            val intent = Intent(this, ContentActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)

        }
        getHappyPlacesListFromLocalDB()
    }
    private fun setupHappyPlacesRecyclerview(happyPlaceList: ArrayList<HappyPlaceModel>){
        rv_happy_places_list.layoutManager = LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)

        val placesAdapter = HappyPlacesAdapter(this, happyPlaceList)
        rv_happy_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : HappyPlacesAdapter.OnclickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@WedinActivity,
                    HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

    }

    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        val getHappyPlaceList : ArrayList<HappyPlaceModel> = dbHandler.getHappyPlacesList()
        if (getHappyPlaceList.size > 0){
            rv_happy_places_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setupHappyPlacesRecyclerview(getHappyPlaceList)
        }else{
            rv_happy_places_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                getHappyPlacesListFromLocalDB()
            }else{
                Log.e("Activity", "Cancelled or Back pressed")
            }
        }
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

    companion object{
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
    }
