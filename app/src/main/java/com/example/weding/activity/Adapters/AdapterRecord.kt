package com.example.weding.activity.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weding.R
import com.example.weding.activity.Activity.RecordDetailActivity
import com.example.weding.activity.model.HappyPlaceModel

class AdapterRecord() : RecyclerView.Adapter<AdapterRecord.HolderRecord>() {
    private var context: Context?=null
    private var recordList: ArrayList<HappyPlaceModel>?=null

    constructor(context: Context?, recordList: ArrayList<HappyPlaceModel>?) : this() {
        this.context = context
        this.recordList = recordList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        return HolderRecord(
            LayoutInflater.from(context).inflate(R.layout.event_list, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return recordList!!.size
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {
        val model = recordList!!.get(position)

        val id = model.id
        val image = model.image
        val name = model.description
        val couple = model.title
        val address = model.location
        val date = model.date

        holder.nameTv.text = name
        holder.couple.text = couple
        holder.address.text = address
        holder.date.text = date
        if (image == "null"){
            holder.profile.setImageResource(R.drawable.ellipse2)
        }
        else{
            holder.profile.setImageURI(Uri.parse(image))
        }

        holder.itemView.setOnClickListener {
           val intent = Intent(context, RecordDetailActivity::class.java)
            intent.putExtra("RECORD_ID", id)
            context!!.startActivity(intent)
        }

    }
    inner class HolderRecord(itemView: View): RecyclerView.ViewHolder(itemView){

        var profile: ImageView = itemView.findViewById(R.id.iv_place_image)
        var nameTv: TextView = itemView.findViewById(R.id.name)
        var couple: TextView = itemView.findViewById(R.id.couple2)
        var date: TextView = itemView.findViewById(R.id.date)
        var address: TextView = itemView.findViewById(R.id.address)
    }

}