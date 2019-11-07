package com.example.pharmacydictionary.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pharmacydictionary.Model.Drug
import com.example.pharmacydictionary.R

class DrugRecyclerAdapter(val context: Context, val drugList: List<Drug>, val itemClick: (Drug) -> Unit) : RecyclerView.Adapter<DrugRecyclerAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.drug_list_item, parent, false)
        return Holder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return drugList.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindDrugInfo(drugList[position])
    }

    inner class Holder(itemView: View, val itemClick: (Drug) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val drugName = itemView.findViewById<TextView>(R.id.drugName)

        fun bindDrugInfo(drug: Drug) {
            drugName.text = drug.name
            itemView.setOnClickListener {
                itemClick(drug)
            }
        }
    }
}