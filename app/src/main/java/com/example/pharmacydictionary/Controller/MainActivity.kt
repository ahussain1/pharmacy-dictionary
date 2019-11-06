package com.example.pharmacydictionary.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmacydictionary.DrugRecyclerAdapter
import com.example.pharmacydictionary.R
import com.example.pharmacydictionary.Services.DataService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: DrugRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecycleView()

    }

    fun setupRecycleView() {

        adapter = DrugRecyclerAdapter(this, DataService.drugs)
        drugListView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        drugListView.layoutManager = layoutManager
        drugListView.setHasFixedSize(true)
    }
}
