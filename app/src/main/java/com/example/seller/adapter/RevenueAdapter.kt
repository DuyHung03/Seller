package com.example.seller.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seller.R
import com.example.seller.entity.Order
import com.example.seller.utils.GlideImageLoader
import com.makeramen.roundedimageview.RoundedImageView

class RevenueAdapter(
    private val itemList: List<Order>,
    private val onItemClicked: (Order) -> Unit
) :
    RecyclerView.Adapter<RevenueAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<RoundedImageView>(R.id.image)
        private val date = itemView.findViewById<TextView>(R.id.date)
        private val income = itemView.findViewById<TextView>(R.id.income)
        private val orderId = itemView.findViewById<TextView>(R.id.orderId)


        @SuppressLint("SetTextI18n")
        fun bind(
            imageUrl: String,
            orderDate: String,
            orderPrice: String,
            id: String
        ) {
            GlideImageLoader(itemView.context).load(
                imageUrl, image, R.drawable.image_placeholder, R.drawable.image_placeholder
            )

            date.text = orderDate
            income.text = "+ $$orderPrice"
            orderId.text = id

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.revenue_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = itemList[position]

        holder.bind(
            order.productList[0].product.images[0],
            order.createdTime.toString(),
            order.cost.productsCost.toString(),
            order.orderId
        )

        holder.itemView.setOnClickListener {
            onItemClicked(order)
        }

    }
}