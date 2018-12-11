package com.androidexample.blacktax

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.androidexample.blacktax.Networking.RecycleDTO
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycle_item.view.*
import java.util.*

class Adapter(val myList: MutableList<RecycleDTO>): RecyclerView.Adapter<Adapter.ViewHolder>() {
    var currentRow: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycle_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: Adapter.ViewHolder, position: Int) {
        holder.titleView.setText(myList[position].title)

        // Note: Image URL cannot be null.
        // URL can be a non-existant URL but does need to be something.
        if (myList[position].imageBlogURL != "") {
            Picasso.get().load(myList[position].imageBlogURL).into(holder.imageView)
        } else {
            // Load default image.
            Picasso.get().load(R.drawable.no_image).into(holder.imageView)
        }
    }


    inner class ViewHolder (val v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        // No findViewById() because this version of Android Studio uses
        //    kotlin-android-extensions plugin, which avoids the use of needing findViewById().
        val titleView: TextView = v.txtTitle
        val imageView: ImageView = v.imgBlogGraphic



        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.i("!!!", "clicked on row: " +  titleView.text)

            val position  = layoutPosition

            val webViewDataArray = ArrayList<String>(4)
            webViewDataArray.add(0, myList.get(position).date)
            webViewDataArray.add(1, myList.get(position).title)
            webViewDataArray.add(2, myList.get(position).imageBlogURL)
            webViewDataArray.add(3, myList.get(position).htmlArticle)

            val mIntentWebViewActivity = Intent(v?.context, WebViewActivity::class.java)
            // pass in some data to the intent.
            mIntentWebViewActivity.putStringArrayListExtra(ProjectData.putExtra_BlogWebView, webViewDataArray)
            v?.context?.startActivity(mIntentWebViewActivity)
        }
    }
}
