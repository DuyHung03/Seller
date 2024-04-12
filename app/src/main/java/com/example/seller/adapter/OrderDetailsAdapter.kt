package com.example.seller.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seller.R
import com.example.seller.entity.CartItem
import com.example.seller.utils.GlideImageLoader
import com.makeramen.roundedimageview.RoundedImageView

class OrderDetailsAdapter(
    private val itemList: List<CartItem>,
) :
    RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<RoundedImageView>(R.id.image)
        private val productName = itemView.findViewById<TextView>(R.id.productName)
        private val price = itemView.findViewById<TextView>(R.id.price)
        private val quantity = itemView.findViewById<TextView>(R.id.quantity)
        private val productId = itemView.findViewById<TextView>(R.id.productId)

        @SuppressLint("SetTextI18n")
        fun bind(
            imageUrl: String,
            name: String,
            productQuantity: String,
            productPrice: String,
            id: String
        ) {
            GlideImageLoader(itemView.context).load(
                imageUrl, image, R.drawable.image_placeholder, R.drawable.image_placeholder
            )

            productName.text = name

            price.text = "$ $productPrice"

            quantity.text = productQuantity

            productId.text = id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_in_details, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = itemList[position]

        holder.bind(
            cartItem.product.images[0],
            cartItem.product.title,
            cartItem.quantity.toString(),
            cartItem.product.price.toString(),
            cartItem.productId
        )

    }
}