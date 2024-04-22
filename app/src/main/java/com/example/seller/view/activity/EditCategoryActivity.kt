package com.example.seller.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.seller.R
import com.example.seller.databinding.ActivityEditCategoryBinding
import com.example.seller.entity.Category
import com.example.seller.entity.CategoryRequest
import com.example.seller.utils.GlideImageLoader
import com.example.seller.viewmodel.DataViewModel
import com.example.seller.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBinding
    private val productViewModel by viewModels<ProductViewModel>()
    private val dataViewModel by viewModels<DataViewModel>()
    private var imageUri: String = ""

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)
        binding = ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            val category: Category? = intent.getParcelableExtra("category")
            if (category != null) {
                setupContentCategory(category)
                handleCategory(category)
            } else {
                binding.catId.visibility = View.GONE
                binding.editBtnsLayout.visibility = View.GONE
                binding.addNewBtn.visibility = View.VISIBLE
                binding.header.text = "New category"
            }
        }

        binding.closeBtn.setOnClickListener {
            this.finish()
        }

    }

    override fun onResume() {
        super.onResume()
        binding.addNewBtn.setOnClickListener {
            addNewCategory()
        }

        binding.addPhotoBtn.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } 

    }

    private fun addNewCategory() {
        binding.progressBar.visibility = View.VISIBLE

        val categoryRequest = CategoryRequest(
            imageUri,
            binding.edtTitle.text.toString()
        )

        productViewModel.addNewCategory(categoryRequest) { message ->
            if (message == "Success") {
                Toast.makeText(this, "Add new category successfully", Toast.LENGTH_SHORT).show()
                this.finish()
            } else {
                Log.d("TAG", "handleProduct: $message")
                Toast.makeText(this, "Add new category failed: $message", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun handleCategory(category: Category) {
        //update category
        binding.saveBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val categoryRequest = CategoryRequest(
                imageUri,
                binding.edtTitle.text.toString()
            )
            productViewModel.updateCategory(category.id, categoryRequest) { message ->
                if (message == "Success") {
                    Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show()
                    this.finish()
                } else {
                    Log.d("TAG", "handleCategory: $message")
                    Toast.makeText(this, "Category updated failed: $message", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.progressBar.visibility = View.GONE
            }
        }

        //delete category
        binding.deleteBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            productViewModel.deleteCategory(category.id) { message ->
                if (message == "Success") {
                    Toast.makeText(this, "Category deleted successfully", Toast.LENGTH_SHORT).show()
                    this.finish()
                } else {
                    Log.d("TAG", "handleCategory: $message")
                    Toast.makeText(this, "Category deleted failed: $message", Toast.LENGTH_SHORT)
                        .show()
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupContentCategory(category: Category) {
        binding.catId.text = "Category ID: ${category.id}"
        binding.edtTitle.setText(category.name)
        imageUri = category.image
        GlideImageLoader(this).load(
            category.image,
            binding.image,
            R.drawable.image_placeholder,
            R.drawable.image_placeholder
        )
    }
}