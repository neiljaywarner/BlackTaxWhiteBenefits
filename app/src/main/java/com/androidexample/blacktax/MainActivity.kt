package com.androidexample.blacktax

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var myList = mutableListOf<String>()
        for (i in 0..20) {
            myList.add(i,"Record " + i.toString())
        }

        val llm = LinearLayoutManager(this)
        val adapter = Adapter(myList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=llm

    }
}
