package com.example.weding.activity.Activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weding.R
import com.example.weding.activity.Activity.model.HappyPlaceModel
import kotlinx.android.synthetic.main.activity_happy_place_detail.*

class HappyPlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_happy_place_detail)
        var happyPlaceDetailModel : HappyPlaceModel ?= null
        if (intent.hasExtra(WedinActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel =
                intent.getSerializableExtra(
                    WedinActivity.EXTRA_PLACE_DETAILS) as HappyPlaceModel
        }
        if (happyPlaceDetailModel!= null){
            setSupportActionBar(toolbar_happy_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = happyPlaceDetailModel.title

            toolbar_happy_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }
            iv_image.setImageURI(Uri.parse(happyPlaceDetailModel.image))
            tv_name.text = happyPlaceDetailModel.description
            tv_description.text = happyPlaceDetailModel.title
            tv_address.text = happyPlaceDetailModel.location
            tv_date.text = happyPlaceDetailModel.date
        }
    }
}