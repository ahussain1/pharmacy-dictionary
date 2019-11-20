package com.example.pharmacydictionary.Controller
import android.content.Context
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
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.ActionBarDrawerToggle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MenuItem
import android.graphics.Color
import android.view.View
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var dl: DrawerLayout? = null
    private var t: ActionBarDrawerToggle? = null
    private var nv: NavigationView? = null

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

        // Configure action bar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Pharmacy Dictionary"


        // Initialize the action bar drawer toggle instance
        val drawerToggle:ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ){
            override fun onDrawerClosed(view:View){
                super.onDrawerClosed(view)
                //toast("Drawer closed")
            }

            override fun onDrawerOpened(drawerView: View){
                super.onDrawerOpened(drawerView)
                //toast("Drawer opened")
            }
        }


        // Configure the drawer layout to add listener and show icon on toolbar
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_cut -> {
                loadFavorites(favorites = FavoritesFragment())
            }
            R.id.action_copy -> toast("Copy clicked")
            R.id.action_paste -> toast("Paste clicked")
            R.id.action_new ->{
                // Multiline action
                toast("New clicked")
                drawer_layout.setBackgroundColor(Color.RED)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
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

    private fun Context.toast(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }

    private fun loadFavorites(favorites: FavoritesFragment) {
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.drawer_layout, favorites)
        fm.commit()
    }
}
