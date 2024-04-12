package com.example.seller.view.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seller.R
import com.example.seller.adapter.OrderAdapter
import com.example.seller.databinding.FragmentHomeBinding
import com.example.seller.entity.Order
import com.example.seller.view.activity.AllOrdersActivity
import com.example.seller.view.activity.OrderDetailsActivity
import com.example.seller.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var orderAdapter: OrderAdapter
    private var list = mutableListOf<Order>()
    private val dataViewModel by viewModels<DataViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        dataViewModel.ordersLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                list.clear()
                for (order in it) {
                    if (!order.confirmed && !order.cancelled) {
                        list.add(order)
                    }
                }
                if (list.isEmpty()) {
                    binding.emptyLayout.visibility = View.VISIBLE
                } else {
                    setupRecyclerView()
                    binding.emptyLayout.visibility = View.GONE
                }
                binding.progressBar.visibility = View.GONE
            } else {
                Toast.makeText(context, "Error while get orders", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.emptyLayout.visibility = View.VISIBLE
            }
        }

        binding.orderBtn.setOnClickListener {
            //see all orders
            toAllOrdersScreen()
        }

        binding.revenueBtn.setOnClickListener {
            val fm: FragmentManager = childFragmentManager
            val ft = fm.beginTransaction()
            val revenueFragment = RevenueFragment()
            ft.replace(R.id.fragment_container, revenueFragment)
            ft.commit()
        }

        binding.swipeLayout.setOnRefreshListener {
            dataViewModel.getOrders()
            binding.swipeLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        dataViewModel.getOrders()
    }

    private fun toAllOrdersScreen() {
        val intent = Intent(context, AllOrdersActivity::class.java)
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

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(list) { order ->
            toOrderDetailsScreen(order)
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = orderAdapter
    }

    private fun toOrderDetailsScreen(order: Order) {
        val intent = Intent(context, OrderDetailsActivity::class.java)
        intent.putExtra("order", order)
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

}