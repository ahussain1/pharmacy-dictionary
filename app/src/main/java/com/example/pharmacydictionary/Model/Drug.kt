package com.example.pharmacydictionary.Model

import java.io.Serializable

class Drug(val name: String, val uses: String, val instruction: String, val side_effects: String,
           val precaution: String, val storage: String) : Serializable

