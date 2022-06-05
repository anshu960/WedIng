package com.example.weding.activity

import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weding.R
import com.example.weding.activity.database.DataEvent

class Adapter(val menuList: ArrayList<DataEvent>) : RecyclerView.Adapter<Adapter.MyviewHolder>(),
    android.widget.Adapter {
    class MyviewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val text:TextView = itemView.findViewById(R.id.couple)
        val text2:TextView = itemView.findViewById(R.id.couple2)
        val text3:TextView = itemView.findViewById(R.id.date_txt)
        val text4:TextView = itemView.findViewById(R.id.address)
        val coupleimg:ImageView = itemView.findViewById(R.id.couple_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyviewHolder {
        val viewHolder= LayoutInflater.from(parent.context).inflate(R.layout.event_list,parent,false)
        return MyviewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: MyviewHolder, position: Int) {
          holder.text.text = menuList[position].coupleName1
          holder.text2.text = menuList[position].coupleName2
          holder.text3.text = menuList[position].date
          holder.text4.text = menuList[position].address

        holder.coupleimg.setImageResource(menuList[position].coupleimg)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }


}