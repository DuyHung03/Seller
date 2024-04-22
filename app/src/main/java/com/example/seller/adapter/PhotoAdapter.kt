package com.example.seller.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seller.R
import com.example.seller.utils.GlideImageLoader
import com.google.android.material.textview.MaterialTextView
import com.makeramen.roundedimageview.RoundedImageView

class PhotoAdapter(
    private var itemList: List<String>,

    private val onRemoveClicked: () -> Unit
) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<RoundedImageView>(R.id.image)
        val removeBtn = itemView.findViewById<MaterialTextView>(R.id.removeBtn)!!

        @SuppressLint("SetTextI18n")
        fun bind(
            imageUrl: String,
        ) {
            GlideImageLoader(itemView.context).load(
                imageUrl,
                image,
                R.drawable.image_placeholder,
                R.drawable.image_placeholder
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = itemList[position]

        holder.bind(uri)

        holder.removeBtn.setOnClickListener {
            onRemoveClicked()
        }

    }

    fun setData(list: List<String>) {
        itemList = list
        notifyDataSetChanged()
    }

}