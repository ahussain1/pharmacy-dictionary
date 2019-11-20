package com.example.pharmacydictionary.Controller

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pharmacydictionary.R

class FavoritesFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  LayoutInflater.from(container?.context).inflate(R.layout.favorites_layout, container, false)
        return view
    }
}