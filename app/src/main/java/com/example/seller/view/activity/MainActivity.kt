package com.example.seller.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.seller.R
import com.example.seller.databinding.ActivityMainBinding
import com.example.seller.view.fragment.HomeFragment
import com.example.seller.view.fragment.ProductFragment
import com.example.seller.view.fragment.RevenueFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fm: FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction().replace(R.id.fragment_container, HomeFragment())
        ft.commit()
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it) {
                0 -> {
                    fm.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }

                1 -> {
                    fm.beginTransaction()
                        .replace(R.id.fragment_container, RevenueFragment())
                        .commit()
                }

                2 -> {
                    fm.beginTransaction()
                        .replace(R.id.fragment_container, ProductFragment())
                        .commit()
                }
            }
        }

    }
}