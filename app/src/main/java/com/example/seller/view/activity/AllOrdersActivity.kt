package com.example.seller.view.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seller.R
import com.example.seller.adapter.OrderAdapter
import com.example.seller.databinding.ActivityAllOrdersBinding
import com.example.seller.entity.Order
import com.example.seller.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllOrdersBinding
    private val dataViewModel by viewModels<DataViewModel>()
    private var list: MutableList<Order> = mutableListOf()
    private var pendingList: MutableList<Order> = mutableListOf()
    private var confirmedList: MutableList<Order> = mutableListOf()
    private var cancelledList: MutableList<Order> = mutableListOf()
    private lateinit var orderAdapter: OrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_orders)
        binding = ActivityAllOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE
        dataViewModel.getOrders()
        dataViewModel.ordersLiveData.observe(this) {
            it?.let {
                list.addAll(it)
                setupRecyclerView(list)

                for (order in it) {
                    //pending:  not confirmed yet - not cancelled yet
                    if (!order.confirmed && !order.cancelled) {
                        pendingList.add(order)
                    }
                    //cancelled: not confirmed yet - cancelled
                    if (!order.confirmed && order.cancelled) {
                        cancelledList.add(order)
                    }
                    //success: confirmed - not cancelled yet
                    if (order.confirmed && !order.cancelled) {
                        confirmedList.add(order)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.closeBtn.setOnClickListener {
            this.finish()
        }

        binding.spinner.setItems("All", "Pending", "Confirmed", "Cancelled")

        binding.spinner.setOnItemSelectedListener { _, _, _, item ->
            val list: List<Order> = when (item.toString()) {
                "All" -> list

                "Pending" -> pendingList

                "Confirmed" -> confirmedList

                "Cancelled" -> cancelledList
                else -> list
            }
            setupRecyclerView(list)
        }
    }

    private fun setupRecyclerView(list: List<Order>) {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
        orderAdapter = OrderAdapter(list) { order ->
            toOrderDetailsScreen(order)
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.orderTitle.text = getString(R.string.orders_title, list.size.toString())
        binding.recyclerView.adapter = orderAdapter
        binding.recyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun toOrderDetailsScreen(order: Order) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra("order", order)
        startActivity(
            intent,
            ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
                .toBundle()
        )
    }
}