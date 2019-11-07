package com.example.pharmacydictionary.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmacydictionary.Adapters.DrugRecyclerAdapter
import com.example.pharmacydictionary.Model.Drug
import com.example.pharmacydictionary.R
import com.example.pharmacydictionary.Services.DataService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: DrugRecyclerAdapter
    var displayList:MutableList<Drug> = ArrayList()
    //val mutableList = mutableListOf<Kolory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayList.addAll(DataService.drugs)
        setupRecycleView()
    }

    fun setupRecycleView() {
        adapter = DrugRecyclerAdapter(this, displayList) {drug ->
            val drugIntent = Intent(this, DrugSummaryActivity::class.java)
            startActivity(drugIntent)
        }
        drugListView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        drugListView.layoutManager = layoutManager
        drugListView.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        if(searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        displayList.clear()
                        val search = newText.toLowerCase()

                        DataService.drugs.forEach{
                             if(it.name.toLowerCase().contains(search)) {
                                displayList.add(it)
                             }
                         }

                        drugListView.adapter?.notifyDataSetChanged()
                    } else {
                        displayList.clear()
                        displayList.addAll(DataService.drugs)
                        drugListView.adapter?.notifyDataSetChanged()
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
            })
        }
        return true
    }
}
