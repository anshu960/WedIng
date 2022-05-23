package com.example.weding.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weding.R
import com.example.weding.activity.database.DataEvent
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
        val list = ArrayList<DataEvent>()
        val adapterevent: Adapter?
        edit_floating_button.setOnClickListener {
            val intent = Intent(this, ContentActivity::class.java)
            startActivity(intent)
        }

        list.add(
            DataEvent(
                R.drawable.ellipse2, "Anshu Singh",
                "Nisha Singh",
                "08 Dec 2020",
                "13 wazirpur press area, near dd motors Wazirpur, New Delhi, Delhi 110052",0
            )
        )
        list.add(
            DataEvent(
                R.drawable.ellipse3, "Manik Kathuriya",
                "Anupriya Sharma",
                "20 Feb 2020",
                "13 wazirpur press area, near dd motors Wazirpur, New Delhi, Delhi 110052",1
            )
        )
        list.add(
            DataEvent(
                R.drawable.ellipse4, "Deepali Kathuriya",
                "Ayush Sharma",
                "12 March 2020",
                "13 wazirpur press area, near dd motors Wazirpur, New Delhi, Delhi 110052",2
            )
        )
        list.add(
            DataEvent(
                R.drawable.ellipse2, "Rahul Kathuriya",
                "Priya Sharma",
                "12 June 2010",
                "Village+P.O- Nagpura, Police Station- Simari, District- Buxar 802118",3
            )
        )

        list.add(
            DataEvent(
                R.drawable.ellipse2, "Anshu Singh",
                "Nisha Singh",
                "08 Dec 2020",
                "N/20 Professor Colony, near Orange Inn Munna Chowk Patna, Kankarbagh Patna 20",4
            )
        )
        list.add(
            DataEvent(
                R.drawable.ellipse3, "Manik Kathuriya",
                "Anupriya Sharma",
                "20 Feb 2020",
                "13 wazirpur press area, near dd motors Wazirpur, New Delhi, Delhi 110052",5
            )
        )
        list.add(
            DataEvent(
                R.drawable.ellipse4, "Deepali Kathuriya",
                "Ayush Sharma",
                "12 March 2020",
                "13 wazirpur press area, near dd motors Wazirpur, New Delhi, Delhi 110052",6
            )
        )
        list.add(
            DataEvent(
                R.drawable.ellipse2, "Rahul Kathuriya",
                "Priya Sharma",
                "12 June 2010",
                "Village+P.O- Nagpura, Police Station- Simari, District- Buxar 802118",7
            )
        )


        adapterevent = Adapter(list)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapterevent


        Add_btn.setOnClickListener {
            onAddButtonClicked()
        }
        edit_floating_button.setOnClickListener {
            startActivity(Intent(this, ContentActivity::class.java))
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked){
            edit_floating_button.visibility = View.VISIBLE
        }else{
            edit_floating_button.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            edit_floating_button.startAnimation(fromBottom)
            Add_btn.startAnimation(rotateOpen)
        }else{
            edit_floating_button.startAnimation(toBottom)
            Add_btn.startAnimation(rotateClose)
        }


}
}











