package com.androidexample.blacktax

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.recycle_item.view.*

class Adapter(val myList: MutableList<String>): RecyclerView.Adapter<Adapter.ViewHolder>() {
   var currentRow: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
        holder.title.setText(myList.get(position))
        this.currentRow=position
    }


    class ViewHolder (val v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        // No findViewById() because this version of Android Studio uses
        //    kotlin-android-extensions plugin, which avoids the use of needing findViewById().
        val title: TextView = v.txtTitle

        init {
            v.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            Log.i("!!!", "clicked on row: " +  title.text)
        }
    }
}
