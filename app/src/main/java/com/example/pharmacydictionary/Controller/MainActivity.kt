package com.example.pharmacydictionary.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharmacydictionary.Adapters.DrugRecyclerAdapter
import com.example.pharmacydictionary.Model.Drug
import com.example.pharmacydictionary.R

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: DrugRecyclerAdapter
    var displayList = mutableListOf<Drug>()
    val db = FirebaseFirestore.getInstance()
    val drugList = mutableListOf<Drug>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAllData()
        setupRecycleView()
        println("drugs:" + drugList.toString())
    }

    fun setupRecycleView() {
//        adapter = DrugRecyclerAdapter(this, displayList) {drug ->
//            val drugIntent = Intent(this, DrugSummaryActivity::class.java)
//            startActivity(drugIntent)
//        }
//        drugListView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        drugListView.layoutManager = layoutManager
        drugListView.setHasFixedSize(true)
    }

    fun getAllData() {
        if(drugList.isEmpty()) {
            db.collection("drugs").get().addOnSuccessListener { result ->
                for (document in result) {
                    val name = document["name"].toString()
                    val uses = document["uses"].toString()
                    val instruction = document["instruction"].toString()
                    val side_effects = document["side_effects"].toString()
                    val precaution = document["precaution"].toString()
                    val storage = document["storage"].toString()

                    val drug = Drug(name, uses, instruction, side_effects, precaution, storage)

                    drugList.add(drug)
                }

                adapter = DrugRecyclerAdapter(this, drugList) { drug ->
                    val drugIntent = Intent(this, DrugSummaryActivity::class.java)
                    drugIntent.putExtra("drug", drug)
                    startActivity(drugIntent)
                }
                drugListView.adapter = adapter
            }
        }
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

                        drugList.forEach{
                             if(it.name.toLowerCase().contains(search)) {
                                displayList.add(it)
                             }
                         }

                        drugListView.adapter?.notifyDataSetChanged()
                    } else {
                        displayList.clear()
                        displayList.addAll(drugList)
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
