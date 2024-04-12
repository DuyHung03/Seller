package com.example.seller.view.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.seller.R
import com.example.seller.databinding.FragmentRevenueBinding
import com.example.seller.entity.Order
import com.example.seller.view.activity.RevenueDetailsActivity
import com.example.seller.viewmodel.DataViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class RevenueFragment : Fragment() {
    private lateinit var binding: FragmentRevenueBinding
    private val dataViewModel by viewModels<DataViewModel>()
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRevenueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataViewModel.ordersLiveData.observe(viewLifecycleOwner) {
            it?.let {
                setDataOrderPieChart(it)
                setMonthlyDetails(it)
                seTotalRevenueDetails(it)

                binding.totalButton.setOnClickListener {
                    toRevenueDetails()
                }

                binding.monthlyButton.setOnClickListener {
                    toRevenueDetails()
                }
            }
        }


    }

    private fun toRevenueDetails() {
        val intent = Intent(context, RevenueDetailsActivity::class.java)
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(
                context,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
                .toBundle()
        )
    }

    private fun seTotalRevenueDetails(list: List<Order>) {
        val successList = list.filter { order ->
            order.confirmed
        }
        val revenue = successList.sumOf { order ->
            order.cost.productsCost
        }
        val orders = successList.size

        binding.totalRevenue.text = "$ ${revenue}"
        binding.totalOrders.text = orders.toString()
    }

    private fun setMonthlyDetails(list: List<Order>) {
        // Filter orders for the current month
        val ordersForCurrentMonth = list.filter { order ->
            val orderMonth =
                Calendar.getInstance().apply { time = order.createdTime }.get(Calendar.MONTH) + 1
            orderMonth == currentMonth && order.confirmed
        }

        val revenue = ordersForCurrentMonth.sumOf { order ->
            order.cost.productsCost
        }
        val orders = ordersForCurrentMonth.size

        binding.month.text = currentMonth.toString()
        binding.monthlyRevenue.text = "$ ${revenue}"
        binding.monthlyOrders.text = orders.toString()

    }

    override fun onResume() {
        super.onResume()
        dataViewModel.getOrders()
    }

    private fun setDataOrderPieChart(dataSet: List<Order>) {
        //array color of pie chart
        val colors = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.chart_green),
            ContextCompat.getColor(requireContext(), R.color.chart_orange),
            ContextCompat.getColor(requireContext(), R.color.chart_red)
        )

        var successList = 0
        var pendingList = 0
        var cancelledList = 0
        for (order in dataSet) {
            //pending:  not confirmed yet - not cancelled yet
            if (!order.confirmed && !order.cancelled) {
                pendingList += 1
            }
            //cancelled: not confirmed yet - cancelled
            if (!order.confirmed && order.cancelled) {
                cancelledList += 1
            }
            //success: confirmed - not cancelled yet
            if (order.confirmed && !order.cancelled) {
                successList += 1
            }
        }
        val successEntry = PieEntry(successList.toFloat(), "Success")
        val pendingEntry = PieEntry(pendingList.toFloat(), "Pending")
        val cancelledEntry = PieEntry(cancelledList.toFloat(), "Cancel")

        val entries = listOf(successEntry, pendingEntry, cancelledEntry)

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.colors = colors.toList()
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.valueTextSize = 18F
        pieDataSet.valueTypeface

        binding.orderPieChart.centerText = "${successList + pendingList + cancelledList} orders"
        binding.orderPieChart.centerTextRadiusPercent = 50F
        binding.orderPieChart.holeRadius = 30F
        binding.orderPieChart.invalidate()
        binding.orderPieChart.valuesToHighlight()
        binding.orderPieChart.setDrawEntryLabels(false)
        binding.orderPieChart.description.text = "Manage orders"
        binding.orderPieChart.setTransparentCircleAlpha(0)
        binding.orderPieChart.data = PieData(pieDataSet)
    }

}