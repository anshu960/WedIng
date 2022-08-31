package com.example.weding.activity.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.viewpager2.widget.ViewPager2
import com.example.weding.R
import com.example.weding.activity.Activity.adapter.ViewPagerAdapter
import com.example.weding.activity.Activity.model.HappyPlaceModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_content.*
import kotlinx.android.synthetic.main.activity_happy_place_detail.*
import kotlinx.android.synthetic.main.activity_invitation.*

class InvitationActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invitation)
        actionBar = supportActionBar!!
        actionBar.title = "Invitation"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)



        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
            val viewpager2 = findViewById<ViewPager2>(R.id.view_pager_2)
            val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
            viewpager2.adapter = adapter
            TabLayoutMediator(tabLayout, viewpager2) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Attendees"
                    }
                    1 -> {
                        tab.text = "Non Attendees"
                    }
                    2 -> {
                        tab.text = "Invite"
                    }
                }
            }.attach()



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}