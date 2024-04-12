package com.example.seller.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.seller.entity.ListOrder
import com.example.seller.view.fragment.revenue.AllRevenueFragment
import com.example.seller.view.fragment.revenue.DailyRevenueFragment
import com.example.seller.view.fragment.revenue.MonthlyRevenueFragment
class RevenuePagerAdapter(
    var context: Context,
    fragmentManager: FragmentManager,
    private val listOrder: ListOrder
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AllRevenueFragment.newInstance(listOrder)
            1 -> MonthlyRevenueFragment.newInstance(listOrder)
            2 -> DailyRevenueFragment.newInstance(listOrder)
            else -> AllRevenueFragment.newInstance(listOrder)
        }
    }

    override fun getCount(): Int = 3
}
