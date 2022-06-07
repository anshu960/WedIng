package com.example.weding.activity.Activity

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.weding.R
import com.example.weding.activity.database.SqliteDatabase
import kotlinx.android.synthetic.main.activity_content.view.*
import kotlinx.android.synthetic.main.event_list.view.*

open class EventAdapter ()
      : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var context: Context? = null
    private var list: ArrayList<SqliteDatabase>? = null

    constructor(context: Context, list: ArrayList<SqliteDatabase>?) : this(){
        this.context = context
        this.list = list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.event_list,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list?.get(position)

        if (holder is MyViewHolder){
            if (model != null) {
                holder.itemView.ImageView.setImageURI(Uri.parse(model.image))
            }
            if (model != null) {
                holder.itemView.couple.text = model.coupleName
            }
            if (model != null) {
                holder.itemView.couple2.text = model.coupleName2
            }
            if (model != null) {
                holder.itemView.address.text = model.address
            }
            if (model != null) {
                holder.itemView.date_txt.text = model.date
            }
            if (model != null){
                holder.itemView.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
          val main: ConstraintLayout = itemView.main
          val name: TextView = itemView.couple
          val couple: TextView = itemView.couple2
          val address: TextView = itemView.address
          val date: TextView = itemView.date_txt
          val img: ImageView = itemView.couple_img
    }
}