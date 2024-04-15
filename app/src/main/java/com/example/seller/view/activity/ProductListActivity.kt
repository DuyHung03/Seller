package com.example.seller.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seller.R
import com.example.seller.adapter.ManageViewPagerAdapter
import com.example.seller.databinding.ActivityProductListBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manageViewPagerAdapter = ManageViewPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = manageViewPagerAdapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

    }
}