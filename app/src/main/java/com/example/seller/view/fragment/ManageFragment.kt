package com.example.seller.view.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seller.R
import com.example.seller.databinding.FragmentManageBinding
import com.example.seller.view.activity.ProductListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageFragment : Fragment() {

    private lateinit var binding: FragmentManageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.product.setOnClickListener {
            toProductScreen()
        }
    }

    private fun toProductScreen() {
        val intent = Intent(context, ProductListActivity::class.java)
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