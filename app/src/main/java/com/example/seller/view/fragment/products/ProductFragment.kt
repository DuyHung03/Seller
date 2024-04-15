package com.example.seller.view.fragment.products

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seller.R
import com.example.seller.adapter.ProductListAdapter
import com.example.seller.databinding.FragmentProductBinding
import com.example.seller.entity.Product
import com.example.seller.view.activity.EditProductActivity
import com.example.seller.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private val productViewModel by viewModels<ProductViewModel>()
    private lateinit var binding: FragmentProductBinding
    private val list: MutableList<Product> = mutableListOf()
    private lateinit var productListAdapter: ProductListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        productViewModel.getProducts(0, 0)
        productViewModel.productsLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                list.clear()
                list.addAll(it)
                setupRecyclerView()
            }
        }

        binding.addProduct.setOnClickListener {
            toEditScreen()
        }

    }

    private fun setupRecyclerView() {
        productListAdapter = ProductListAdapter(list) {
            toEditScreen(it)
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter = productListAdapter
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun toEditScreen(product: Product? = null) {
        val intent = Intent(context, EditProductActivity::class.java)
        product?.let {
            intent.putExtra("product", product)
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