package com.example.seller.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seller.R
import com.example.seller.entity.Product
import com.example.seller.utils.GlideImageLoader
import com.makeramen.roundedimageview.RoundedImageView

class ProductListAdapter(
    private val itemList: List<Product>,

    private val onItemClicked: (Product) -> Unit
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private var filteredDataList = itemList.toMutableList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<RoundedImageView>(R.id.image)
        private val name = itemView.findViewById<TextView>(R.id.name)

        @SuppressLint("SetTextI18n")
        fun bind(
            imageUrl: String,
            nameText: String
        ) {
            GlideImageLoader(itemView.context).load(
                imageUrl, image, R.drawable.image_placeholder, R.drawable.image_placeholder
            )

            name.text = nameText

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = itemList[position]

        holder.bind(
            product.images[0],
            product.title
        )

        holder.itemView.setOnClickListener {
            onItemClicked(product)
        }

    }


}