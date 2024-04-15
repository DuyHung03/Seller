package com.example.seller.view.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.seller.R
import com.example.seller.adapter.PhotoAdapter
import com.example.seller.databinding.ActivityEditProductBinding
import com.example.seller.entity.Product
import com.example.seller.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductBinding
    private val productViewModel by viewModels<ProductViewModel>()
    private var categoryList: MutableList<String> = mutableListOf()
    private lateinit var photoAdapter: PhotoAdapter

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                Log.d("TAG", "Number of items selected: ${uris.size}")
            } else {
                Log.d("TAG", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel.getCategories()
        productViewModel.categoriesLiveData.observe(this) {
            it?.let {
                for (category in it) {
                    categoryList.add(category.name)
                }
            }
        }

        binding.spinnerCategory.setItems(categoryList)

        intent?.let {
            val product: Product? = intent.getParcelableExtra("product")
            if (product != null) {
                setupContentProduct(product)
            } else {
                binding.productId.visibility = View.GONE
                binding.deleteBtn.visibility = View.GONE
                binding.header.text = "New product"
            }
        }

        binding.addPhotoBtn.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


    }

    private fun showMultiImage(uriList: List<Uri>) {
        photoAdapter = PhotoAdapter(uriList) {

        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = photoAdapter

    }

    override fun onResume() {
        super.onResume()
        binding.spinnerCategory.setOnItemSelectedListener { view, position, id, item ->

        }
    }

    private fun setupContentProduct(product: Product) {
        binding.productId.text = getString(R.string.productId, product.id.toString())
        binding.edtTitle.setText(product.title)
        binding.edtDesc.setText(product.description)
        binding.edtPrice.setText(product.price.toString())
        binding.spinnerCategory.text = product.category.name
    }
}