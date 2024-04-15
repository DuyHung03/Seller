package com.example.seller.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.seller.view.fragment.products.CategoryFragment
import com.example.seller.view.fragment.products.ProductFragment

class ManageViewPagerAdapter(
    var context: Context,
    fragmentManager: FragmentManager,
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> ProductFragment()
        1 -> CategoryFragment()
        else -> ProductFragment()
    }

    override fun getCount(): Int = 2

}