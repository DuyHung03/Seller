package com.example.seller.view.fragment.products

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.seller.R
import com.example.seller.adapter.CategoryAdapter
import com.example.seller.databinding.FragmentCategoryBinding
import com.example.seller.entity.Category
import com.example.seller.view.activity.EditCategoryActivity
import com.example.seller.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private val productViewModel by viewModels<ProductViewModel>()
    private lateinit var binding: FragmentCategoryBinding
    private val list: MutableList<Category> = mutableListOf()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel.getCategories()
        productViewModel.categoriesLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                list.clear()
                list.addAll(it)
                setupRecyclerView()
            }
        }

        binding.addCategory.setOnClickListener {
            toEditScreen()
        }

    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(list) {
            toEditScreen(it)
        }

        binding.recyclerView.layoutManager =
            GridLayoutManager(context, 2)

        binding.recyclerView.adapter = categoryAdapter
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun toEditScreen(category: Category? = null) {
        val intent = Intent(context, EditCategoryActivity::class.java)
        category?.let {
            intent.putExtra("category", category)
        }
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