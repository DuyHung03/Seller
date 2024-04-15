package com.example.seller.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.seller.R
import com.example.seller.utils.GlideImageLoader
import com.makeramen.roundedimageview.RoundedImageView

class PhotoAdapter(
    private var itemList: List<Uri>,

    private val onRemoveClicked: () -> Unit
) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<RoundedImageView>(R.id.image)
        val removeBtn = itemView.findViewById<ImageView>(R.id.removeBtn)!!

        @SuppressLint("SetTextI18n")
        fun bind(
            imageUrl: Uri,
        ) {
            GlideImageLoader(itemView.context).load(
                imageUrl.toString(),
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

        holder.bind(
            uri
        )

        holder.removeBtn.setOnClickListener {
            onRemoveClicked()
        }

    }

    fun setData(list: List<Uri>) {
        itemList = list
        notifyDataSetChanged()
    }

}