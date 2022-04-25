package com.selim.cyrptomarket.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.selim.cyrptomarket.R.layout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
    }
}