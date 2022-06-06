package com.example.weding.activity

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

open class EventAdapter (
     private val context: Context,
     private var list: ArrayList<SqliteDatabase>
     ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.event_list,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            holder.itemView.ImageView.setImageURI(Uri.parse(model.image))
            holder.itemView.couple.text = model.coupleName
            holder.itemView.couple2.text = model.coupleName2
            holder.itemView.address.text = model.address
            holder.itemView.date_txt.text = model.date
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
          val main: ConstraintLayout = view.main
          val name: TextView = view.couple
          val couple: TextView = view.couple2
          val address: TextView = view.address
          val date: TextView = view.date_txt
          val img: ImageView = view.couple_img
    }
}