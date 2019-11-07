package com.example.pharmacydictionary.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pharmacydictionary.Model.Drug
import com.example.pharmacydictionary.R
import kotlinx.android.synthetic.main.activity_drug_summary.*

class DrugSummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug_summary)

        populateFields()
    }

    fun populateFields() {
        val drug = getIntent().getSerializableExtra("drug") as Drug

        drugName.text = drug.name
        uses.text = drug.uses
        sideEffects.text = drug.side_effects
        precaution.text = drug.precaution
        storage.text = drug.storage
    }

}
