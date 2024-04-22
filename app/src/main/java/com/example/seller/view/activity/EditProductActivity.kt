package com.example.seller.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.seller.R
import com.example.seller.adapter.PhotoAdapter
import com.example.seller.databinding.ActivityEditProductBinding
import com.example.seller.entity.Category
import com.example.seller.entity.Product
import com.example.seller.entity.ProductRequest
import com.example.seller.viewmodel.DataViewModel
import com.example.seller.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductBinding
    private val productViewModel by viewModels<ProductViewModel>()
    private var categoryList: ArrayList<Category> = ArrayList()
    private val dataViewModel by viewModels<DataViewModel>()
    private lateinit var photoAdapter: PhotoAdapter
    private var listImage: MutableList<String> = mutableListOf()
    private var categoryId: Int = 0
    private lateinit var adapter: ArrayAdapter<Category>

    private val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                Log.d("TAG", "Number of items selected: ${uris.size}")
                dataViewModel.saveImageToStorage(uris) {
                    for (uri in it) {
                        listImage.add(uri.toString())
                    }
                    showMultiImage(listImage)
                    Toast.makeText(this, "Add photos successfully", Toast.LENGTH_SHORT).show()
                    binding.loading.visibility = View.GONE
                }
            } else {
                binding.loading.visibility = View.GONE
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
                categoryList.addAll(it)
                setupSpinner(categoryList)

//                SharedPreferences.saveResponseToCache(this, Gson().toJson(it), "category_cache")
            }
        }


        intent?.let {
            val product: Product? = intent.getParcelableExtra("product")
            if (product != null) {
                setupContentProduct(product)
                handleProduct(product)
            } else {
                binding.productId.visibility = View.GONE
                binding.editBtnsLayout.visibility = View.GONE
                binding.addNewPrdBtn.visibility = View.VISIBLE
                binding.header.text = "New product"
            }
        }

        binding.closeBtn.setOnClickListener {
            this.finish()
        }

    }

    private fun setupSpinner(categoryList: List<Category>) {
        adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            categoryList
        )

        binding.spinnerCategory.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun showMultiImage(uriList: List<String>) {
        photoAdapter = PhotoAdapter(uriList) {

        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = photoAdapter

    }

    override fun onResume() {
        super.onResume()

        binding.addPhotoBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCategory = binding.spinnerCategory.selectedItem as Category
                    categoryId = selectedCategory.id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.addNewPrdBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            addNewProduct()
        }

    }

    private fun addNewProduct() {
        val productRequest = ProductRequest(
            binding.edtTitle.text.toString(),
            binding.edtPrice.text.toString().toInt(),
            binding.edtDesc.text.toString(),
            categoryId,
            listImage
        )

        productViewModel.addNewProduct(productRequest) { message ->
            if (message == "Success") {
                Toast.makeText(this, "Product created successfully", Toast.LENGTH_SHORT).show()
                this.finish()
            } else {
                Log.d("TAG", "handleProduct: $message")
                Toast.makeText(this, "Product created failed: $message", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupContentProduct(product: Product) {
        binding.productId.text = getString(R.string.productId, product.id.toString())
        binding.edtTitle.setText(product.title)
        binding.edtDesc.setText(product.description)
        binding.edtPrice.setText(product.price.toString())
        categoryId = product.category.id
        Log.d("TAG", "setupContentProduct: $categoryId")
        for (uri in product.images) {
            Log.d("TAG", "setupContentProduct: $uri")
            listImage.add(uri)
        }
        showMultiImage(listImage)
    }

    private fun handleProduct(product: Product) {
        //update product
        binding.saveBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val productRequest = ProductRequest(
                binding.edtTitle.text.toString(),
                binding.edtPrice.text.toString().toInt(),
                binding.edtDesc.text.toString(),
                categoryId,
                listImage,
            )
            Log.d("TAG", "handleProduct: $listImage $categoryId")
            Log.d("TAG", "handleProduct: $productRequest")
            productViewModel.updateProduct(product.id, productRequest) { message ->
                if (message == "Success") {
                    Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                    this.finish()
                } else {
                    Log.d("TAG", "handleProduct: $message")
                    Toast.makeText(this, "Product update failed: $message", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progressBar.visibility = View.GONE
        }

        //delete product
        binding.deleteBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            productViewModel.deleteProduct(product.id) { message ->
                if (message == "Success") {
                    Toast.makeText(this, "Product delete successfully", Toast.LENGTH_SHORT).show()
                    this.finish()
                } else {
                    Log.d("TAG", "handleProduct: $message")
                    Toast.makeText(this, "Product delete failed: $message", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progressBar.visibility = View.GONE

        }

    }

}