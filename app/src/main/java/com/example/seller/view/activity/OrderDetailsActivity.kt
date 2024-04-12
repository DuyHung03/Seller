package com.example.seller.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seller.R
import com.example.seller.adapter.OrderDetailsAdapter
import com.example.seller.databinding.ActivityOrderDetailsBinding
import com.example.seller.entity.Order
import com.example.seller.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding
    private lateinit var order: Order
    private val dataViewModel by viewModels<DataViewModel>()
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        order = intent.getParcelableExtra("order")!!
        if (!order.confirmed && !order.cancelled)
            binding.buttonLayout.visibility = View.VISIBLE
        else
            binding.buttonLayout.visibility = View.GONE
        initContent()

        binding.rejectButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Reject this order")
                .setMessage("Are you sure you want to reject this order?")
                .setPositiveButton(R.string.reject) { _, _ ->
                    rejectOrder()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        binding.acceptButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Accept this order")
                .setMessage("When accepting this order, please prepare all of the products in this order and deliver them as soon as possible")
                .setPositiveButton(R.string.accept) { _, _ ->
                    acceptOrder()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        binding.closeBtn.setOnClickListener {
            this.finish()
        }
    }

    private fun acceptOrder() {
        val message = dataViewModel.acceptOrder(order, order.customerId)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        this.finish()
    }

    @SuppressLint("SetTextI18n")
    private fun initContent() {
        binding.orderId.text = order.orderId

        //address
        val formattedAddress = SpannableStringBuilder()
        formattedAddress.append(Html.fromHtml("<b>${order.address.name}</b>"))
        formattedAddress.append(",  ${order.address.phoneNumber}\n")
        formattedAddress.append("${order.address.city},\n")
        formattedAddress.append("${order.address.ward},\n")
        formattedAddress.append("${order.address.district},\n")
        formattedAddress.append(order.address.houseAddress)
        binding.addressDetail.text = formattedAddress

        setupProductRecyclerView()

        binding.totalOfList.text = "$ ${order.cost.productsCost}"
        binding.shippingCost.text = "$ ${order.cost.shippingCost}"
        binding.total.text = "$ ${order.cost.total}"

        binding.date.text = order.createdTime.toString()

    }

    private fun setupProductRecyclerView() {
        orderDetailsAdapter = OrderDetailsAdapter(order.productList)
        binding.productRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.productRecyclerView.adapter = orderDetailsAdapter
    }

    private fun rejectOrder() {
        val message = dataViewModel.cancelOrder(order, order.customerId)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        this.finish()
    }
}