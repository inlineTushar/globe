package com.globe.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globe.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityHomeBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }
}
